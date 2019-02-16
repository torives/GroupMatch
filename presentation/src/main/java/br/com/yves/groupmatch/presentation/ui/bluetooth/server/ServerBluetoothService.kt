/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.yves.groupmatch.presentation.ui.bluetooth.server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import br.com.yves.groupmatch.data.toByteArray
import br.com.yves.groupmatch.data.toInt
import br.com.yves.groupmatch.presentation.ui.bluetooth.BluetoothMessageHandler
import java.io.*
import java.util.*

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
class ServerBluetoothService
/**
 * Constructor. Prepares a new BluetoothChat session.
 *
 * @param handler A Handler to send messages back to the UI Activity
 */
(private val handler: Handler) {

	// Member fields
	private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
	private var secureAcceptThread: AcceptThread? = null
	private var insecureAcceptThread: AcceptThread? = null
	private val connectedThreads = HashSet<ConnectedThread>()
	private var mState = STATE_NONE

	// Give the new state to the Handler so the UI Activity can update
	private var state: Int
		@Synchronized get() = mState
		@Synchronized private set(state) {
			Log.d(TAG, "setState() $mState -> $state")

			mState = state
			handler.obtainMessage(
					BluetoothMessageHandler.MESSAGE_STATE_CHANGE,
					state,
					-1
			).sendToTarget()
		}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume()
	 */
	@Synchronized
	fun start() {
		Log.d(TAG, "start")

		killAllConnectedThreads()

		state = STATE_LISTEN

		// Start the thread to listen on a BluetoothServerSocket
		if (secureAcceptThread == null) {
			secureAcceptThread = AcceptThread(true)
			secureAcceptThread!!.start()
		}
		if (insecureAcceptThread == null) {
			insecureAcceptThread = AcceptThread(false)
			insecureAcceptThread!!.start()
		}
	}

	@Synchronized
	fun pause() {
		Log.d(TAG, "pause")

		if (secureAcceptThread != null) {
			secureAcceptThread!!.cancel()
			secureAcceptThread = null
		}

		if (insecureAcceptThread != null) {
			insecureAcceptThread!!.cancel()
			insecureAcceptThread = null
		}

		//FIXME: adicionar o estado de pausado
		if(connectedThreads.isEmpty()){
			state = STATE_NONE
		}
	}

	@Synchronized
	fun resume() {
		Log.d(TAG, "resume")
		// Start the thread to listen on a BluetoothServerSocket
		if (secureAcceptThread == null) {
			secureAcceptThread = AcceptThread(true)
			secureAcceptThread!!.start()
		}
		if (insecureAcceptThread == null) {
			insecureAcceptThread = AcceptThread(false)
			insecureAcceptThread!!.start()
		}

		state = STATE_LISTEN
	}

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 *
	 * @param socket The BluetoothSocket on which the connection was made
	 * @param device The BluetoothDevice that has been connected
	 */
	@Synchronized
	fun connected(socket: BluetoothSocket, device: BluetoothDevice, socketType: String) {
		Log.d(TAG, "connected, Socket Type:$socketType")

		// Start the thread to manage the connection and perform transmissions
		val mConnectedThread = ConnectedThread(socket, socketType)
		connectedThreads.add(mConnectedThread)
		mConnectedThread.start()

		state = STATE_CONNECTED


		handler.obtainMessage(
				BluetoothMessageHandler.MESSAGE_DEVICE_CONNECTED,
				device
		).sendToTarget()
	}

	/**
	 * Stop all threads
	 */
	@Synchronized
	fun stop() {
		Log.d(TAG, "stop")

		killAllConnectedThreads()

		if (secureAcceptThread != null) {
			secureAcceptThread!!.cancel()
			secureAcceptThread = null
		}

		if (insecureAcceptThread != null) {
			insecureAcceptThread!!.cancel()
			insecureAcceptThread = null
		}
		state = STATE_NONE
	}

	// Cancel any thread currently running a connection
	private fun killAllConnectedThreads() {
		if (!connectedThreads.isEmpty()) {
			for (thread in connectedThreads) {
				thread.cancel()
			}
			connectedThreads.clear()
		}
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 *
	 * @param out The bytes to write
	 * @see ConnectedThread.write
	 */
	fun write(out: ByteArray) {
		// Create temporary object
		val r: ConnectedThread
		// Synchronize a copy of the ConnectedThread
		synchronized(this) {
			if (mState != STATE_CONNECTED) return

			val totalMessageBytes = out.count().toByteArray()
			val payload = totalMessageBytes.plus(out)

			for (thread in connectedThreads) {
				thread.write(payload)
			}
		}
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private fun lostConnection(device: BluetoothDevice, thread: ConnectedThread) {

		thread.cancel()
		synchronized(this) {
			connectedThreads.remove(thread)
		}

		// Send a failure message back to the Activity
		val msg = handler.obtainMessage(BluetoothMessageHandler.MESSAGE_CONNECTION_LOST).apply {
			obj = device
		}
		handler.sendMessage(msg)
	}

	/**
	 * This thread runs while listening for incoming connections. It behaves
	 * like a server-side client. It runs until a connection is accepted
	 * (or until cancelled).
	 */
	internal inner class AcceptThread(secure: Boolean) : Thread() {
		// The local server socket
		private lateinit var serverSocket: BluetoothServerSocket
		private val socketType: String = if (secure) "Secure" else "Insecure"

		init {
			// Create a new listening server socket
			try {
				serverSocket = if (secure) {
					bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
							MY_UUID_SECURE)
				} else {
					bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
							NAME_INSECURE, MY_UUID_INSECURE)
				}
			} catch (e: IOException) {
				Log.e(TAG, "Socket Type: " + socketType + "listen() failed", e)
			}
		}

		override fun run() {
			Log.d(TAG, "Socket Type: " + socketType +
					"BEGIN mAcceptThread" + this)
			name = "AcceptThread$socketType"

			var socket: BluetoothSocket?

			// Listen to the server socket if we're not connected
			while (true) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					socket = serverSocket.accept()
				} catch (e: IOException) {
					Log.e(TAG, "Socket Type: " + socketType + "accept() failed", e)
					break
				}

				// If a connection was accepted
				if (socket != null) {
					synchronized(this@ServerBluetoothService) {
						connected(socket, socket.remoteDevice,
								socketType)
					}
				}
			}
			Log.i(TAG, "END mAcceptThread, socket Type: $socketType")
		}

		fun cancel() {
			Log.d(TAG, "Socket Type" + socketType + "cancel " + this)
			try {
				serverSocket.close()
				interrupt()
			} catch (e: IOException) {
				Log.e(TAG, "Socket Type" + socketType + "close() of server failed", e)
			}
		}
	}

	/**
	 * This thread runs during a connection with a remote device.
	 * It handles all incoming and outgoing transmissions.
	 */
	internal inner class ConnectedThread(private val socket: BluetoothSocket, socketType: String) : Thread() {
		private lateinit var inStream: InputStream
		private lateinit var outStream: OutputStream

		init {
			Log.d(TAG, "create ConnectedThread: $socketType")

			// Get the BluetoothSocket input and output streams
			try {
				inStream = socket.inputStream
				outStream = socket.outputStream
			} catch (e: IOException) {
				Log.e(TAG, "Failed to retrieve streams", e)
			}
		}

		override fun run() {
			Log.i(TAG, "BEGIN mConnectedThread")
			val reader = BufferedReader(InputStreamReader(inStream))
			while (true) {
				try {
					val message = reader.readLine()
					handler.obtainMessage(
							BluetoothMessageHandler.MESSAGE_READ,
							message
					).sendToTarget()
				} catch (e: IOException) {
					Log.d(TAG, "disconnected")
					lostConnection(socket.remoteDevice, this)
					return
				}
			}
		}

		/**
		 * Write to the connected OutStream.
		 *
		 * @param buffer The bytes to write
		 */
		fun write(buffer: ByteArray) {
			try {
				outStream.write(buffer)

				// Share the sent message back to the UI Activity
				handler.obtainMessage(
						BluetoothMessageHandler.MESSAGE_WRITE,
						-1,
						-1,
						buffer
				).sendToTarget()
			} catch (e: IOException) {
				Log.e(TAG, "Exception during write", e)
			}
		}

		fun cancel() {
			try {
				socket.close()
				inStream.close()
				outStream.close()
				interrupt()
			} catch (e: IOException) {
				Log.e(TAG, "close() of connect socket failed", e)
			}
		}
	}

	companion object {
		// Debugging
		private val TAG = ServerBluetoothService::class.java.simpleName

		// Name for the SDP record when creating server socket
		private const val NAME_SECURE = "BluetoothChatSecure"
		private const val NAME_INSECURE = "BluetoothChatInsecure"

		// Unique UUID for this application
		private val MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
		private val MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

		// Constants that indicate the current connection state
		const val STATE_NONE = 0       // we're doing nothing
		const val STATE_LISTEN = 1     // now listening for incoming connections
		const val STATE_CONNECTING = 2 // now initiating an outgoing connection
		const val STATE_CONNECTED = 3  // now connected to a remote device
	}
}