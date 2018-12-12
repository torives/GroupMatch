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

package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.HashSet
import java.util.UUID

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
(private val mHandler: Handler) {

	// Member fields
	private val mAdapter: BluetoothAdapter
	private var mSecureAcceptThread: AcceptThread? = null
	private var mInsecureAcceptThread: AcceptThread? = null
	private val connectedThreads = HashSet<ConnectedThread>()
	private var mState: Int = 0

	/**
	 * Return the current connection state.
	 */
	/**
	 * Set the current state of the chat connection
	 *
	 * @param state An integer defining the current connection state
	 */
	// Give the new state to the Handler so the UI Activity can update
	var state: Int
		@Synchronized get() = mState
		@Synchronized private set(state) {
			Log.d(TAG, "setState() $mState -> $state")
			mState = state
			mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget()
		}

	init {
		mAdapter = BluetoothAdapter.getDefaultAdapter()
		mState = STATE_NONE
	}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume()
	 */
	@Synchronized
	fun start() {
		Log.d(TAG, "start")

		// Cancel any thread currently running a connection
		if (!connectedThreads.isEmpty()) {
			for (thread in connectedThreads) {
				thread.cancel()
			}
			connectedThreads.clear()
		}

		state = STATE_LISTEN

		// Start the thread to listen on a BluetoothServerSocket
		if (mSecureAcceptThread == null) {
			mSecureAcceptThread = AcceptThread(true)
			mSecureAcceptThread!!.start()
		}
		if (mInsecureAcceptThread == null) {
			mInsecureAcceptThread = AcceptThread(false)
			mInsecureAcceptThread!!.start()
		}
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

		// Send the name of the connected device back to the UI Activity
		val msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME)
		val bundle = Bundle()
		bundle.putString(Constants.DEVICE_NAME, device.name)
		msg.data = bundle
		mHandler.sendMessage(msg)

		state = STATE_CONNECTED
	}

	/**
	 * Stop all threads
	 */
	@Synchronized
	fun stop() {
		Log.d(TAG, "stop")

		if (!connectedThreads.isEmpty()) {
			for (thread in connectedThreads) {
				thread.cancel()
			}
			connectedThreads.clear()
		}

		if (mSecureAcceptThread != null) {
			mSecureAcceptThread!!.cancel()
			mSecureAcceptThread = null
		}

		if (mInsecureAcceptThread != null) {
			mInsecureAcceptThread!!.cancel()
			mInsecureAcceptThread = null
		}
		state = STATE_NONE
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
			for (thread in connectedThreads) {
				thread.write(out)
			}
		}
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private fun connectionLost() {
		// Send a failure message back to the Activity
		val msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST)
		val bundle = Bundle()
		bundle.putString(Constants.TOAST, "Device connection was lost")
		msg.data = bundle
		mHandler.sendMessage(msg)

		// Start the service over to restart listening mode
		this@ServerBluetoothService.start()
	}

	/**
	 * This thread runs while listening for incoming connections. It behaves
	 * like a server-side client. It runs until a connection is accepted
	 * (or until cancelled).
	 */
	internal inner class AcceptThread(secure: Boolean) : Thread() {
		// The local server socket
		private val mmServerSocket: BluetoothServerSocket?
		private val mSocketType: String

		init {
			var tmp: BluetoothServerSocket? = null
			mSocketType = if (secure) "Secure" else "Insecure"

			// Create a new listening server socket
			try {
				if (secure) {
					tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
							MY_UUID_SECURE)
				} else {
					tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
							NAME_INSECURE, MY_UUID_INSECURE)
				}
			} catch (e: IOException) {
				Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e)
			}

			mmServerSocket = tmp
		}

		override fun run() {
			Log.d(TAG, "Socket Type: " + mSocketType +
					"BEGIN mAcceptThread" + this)
			name = "AcceptThread$mSocketType"

			var socket: BluetoothSocket? = null

			// Listen to the server socket if we're not connected
			while (true) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					socket = mmServerSocket!!.accept()
				} catch (e: IOException) {
					Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e)
					break
				}

				// If a connection was accepted
				if (socket != null) {
					synchronized(this@ServerBluetoothService) {
						connected(socket, socket.remoteDevice,
								mSocketType)
						//                    switch (mState) {
						//                        case STATE_LISTEN:
						//                        case STATE_CONNECTING:
						//                            // Situation normal. Start the connected thread.
						//                            connected(socket, socket.getRemoteDevice(),
						//                                    mSocketType);
						//                            break;
						//                        case STATE_NONE:
						//                        case STATE_CONNECTED:
						//                            // Either not ready or already connected. Terminate new socket.
						//                            try {
						//                                socket.close();
						//                            } catch (IOException e) {
						//                                Log.e(TAG, "Could not close unwanted socket", e);
						//                            }
						//                            break;
						//                    }
					}
				}
			}
			Log.i(TAG, "END mAcceptThread, socket Type: $mSocketType")

		}

		fun cancel() {
			Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this)
			try {
				mmServerSocket!!.close()
			} catch (e: IOException) {
				Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e)
			}

		}
	}

	/**
	 * This thread runs during a connection with a remote device.
	 * It handles all incoming and outgoing transmissions.
	 */
	internal inner class ConnectedThread(private val mmSocket: BluetoothSocket, socketType: String) : Thread() {
		private val mmInStream: InputStream?
		private val mmOutStream: OutputStream?

		init {
			Log.d(TAG, "create ConnectedThread: $socketType")
			var tmpIn: InputStream? = null
			var tmpOut: OutputStream? = null

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = mmSocket.inputStream
				tmpOut = mmSocket.outputStream
			} catch (e: IOException) {
				Log.e(TAG, "temp sockets not created", e)
			}

			mmInStream = tmpIn
			mmOutStream = tmpOut
		}

		override fun run() {
			Log.i(TAG, "BEGIN mConnectedThread")
			val buffer = ByteArray(1024)
			var bytes: Int

			// Keep listening to the InputStream while connected
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream!!.read(buffer)

					// Send the obtained bytes to the UI Activity
					mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
							.sendToTarget()
				} catch (e: IOException) {
					Log.e(TAG, "disconnected", e)
					connectionLost()
					// Start the service over to restart listening mode
					this@ServerBluetoothService.start()
					break
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
				mmOutStream!!.write(buffer)

				// Share the sent message back to the UI Activity
				mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
						.sendToTarget()
			} catch (e: IOException) {
				Log.e(TAG, "Exception during write", e)
			}

		}

		fun cancel() {
			try {
				mmSocket.close()
			} catch (e: IOException) {
				Log.e(TAG, "close() of connect socket failed", e)
			}

		}
	}

	companion object {
		// Debugging
		private val TAG = "ServerBluetoothService"

		// Name for the SDP record when creating server socket
		private val NAME_SECURE = "BluetoothChatSecure"
		private val NAME_INSECURE = "BluetoothChatInsecure"

		// Unique UUID for this application
		private val MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
		private val MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

		// Constants that indicate the current connection state
		val STATE_NONE = 0       // we're doing nothing
		val STATE_LISTEN = 1     // now listening for incoming connections
		val STATE_CONNECTING = 2 // now initiating an outgoing connection
		val STATE_CONNECTED = 3  // now connected to a remote device
	}
}
