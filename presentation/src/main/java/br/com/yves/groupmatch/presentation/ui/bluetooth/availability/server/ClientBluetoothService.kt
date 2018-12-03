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
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.BluetoothMessageHandler
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.BluetoothMessageHandler.Companion.MESSAGE_READ
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
class ClientBluetoothService
/**
 * Constructor. Prepares a new BluetoothChat session.
 *
 * @param handler A Handler to send messages back to the UI Activity
 */
(private val handler: BluetoothMessageHandler) {

	// Member fields
	private val adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
	private var connectThread: ConnectThread? = null
	private var connectedThread: ConnectedThread? = null
	private var mState = STATE_NONE

	// Give the new state to the Handler so the UI Activity can update
	var state: Int
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
		Log.i(TAG, "start")

		killAllThreads()
		state = STATE_LISTEN
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 *
	 * @param device The BluetoothDevice to connect
	 * @param secure Socket Security type - Secure (true) , Insecure (false)
	 */
	@Synchronized
	fun connect(device: BluetoothDevice, secure: Boolean) {
		Log.d(TAG, "connect to: $device")

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			connectThread?.cancel()
			connectThread = null
		}

		// Cancel any thread currently running a connection
		connectedThread?.cancel()
		connectedThread = null

		// Start the thread to connect with the given device
		connectThread = ConnectThread(device, secure).apply {
			start()
		}
		state = STATE_CONNECTING
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

		killAllThreads()
		// Start the thread to manage the connection and perform transmissions
		connectedThread = ConnectedThread(socket, socketType).apply {
			start()
		}

		state = STATE_CONNECTED
	}

	/**
	 * Stop all threads
	 */
	@Synchronized
	fun stop() {
		Log.d(TAG, "stop")

		killAllThreads()
		state = STATE_NONE
	}

	@Synchronized
	private fun killAllThreads() {
		// Cancel the thread that completed the connection
		connectThread?.cancel()
		connectThread = null

		// Cancel any thread currently running a connection
		connectedThread?.cancel()
		connectedThread = null
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 *
	 * @param out The bytes to write
	 * @see ConnectedThread.write
	 */
	fun write(out: ByteArray) {
		// Create temporary object
		val r: ConnectedThread?
		// Synchronize a copy of the ConnectedThread
		synchronized(this) {
			if (mState != STATE_CONNECTED) return
			r = connectedThread
		}
		// Perform the write unsynchronized
		r!!.write(out)
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private fun failedToConnectTo(device: BluetoothDevice) {
		// Send a failure message back to the Activity
		val msg = handler.obtainMessage(BluetoothMessageHandler.MESSAGE_CONNECTION_FAILED).apply {
			obj = device
		}
		handler.sendMessage(msg)
		// Start the service over to restart listening mode
		start()
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private fun lostConnectionTo(device: BluetoothDevice) {
		// Send a failure message back to the Activity
		val msg = handler.obtainMessage(BluetoothMessageHandler.MESSAGE_CONNECTION_LOST).apply {
			obj = device
		}
		handler.sendMessage(msg)
		// Start the service over to restart listening mode
		start()
	}

	/**
	 * This thread runs while attempting to make an outgoing connection
	 * with a device. It runs straight through; the connection either
	 * succeeds or fails.
	 */
	private inner class ConnectThread(private val device: BluetoothDevice, secure: Boolean) : Thread() {
		private lateinit var socket: BluetoothSocket
		private val socketType: String = if (secure) "Secure" else "Insecure"

		init {
			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				socket = if (secure) {
					device.createRfcommSocketToServiceRecord(
							MY_UUID_SECURE)
				} else {
					device.createInsecureRfcommSocketToServiceRecord(
							MY_UUID_INSECURE)
				}
			} catch (e: IOException) {
				Log.e(TAG, "Socket Type: " + socketType + "create() failed", e)
			}
		}

		override fun run() {
			Log.i(TAG, "BEGIN connectThread SocketType:$socketType")
			name = "ConnectThread$socketType"

			// Always cancel discovery because it will slow down a connection
			adapter.cancelDiscovery()

			// Make a connection to the BluetoothSocket
			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				socket.connect()
			} catch (e: IOException) {
				// Close the socket
				try {
					socket.close()
				} catch (e2: IOException) {
					Log.e(TAG, "unable to close() " + socketType +
							" socket during connection failure", e2)
				}
				failedToConnectTo(socket.remoteDevice)
				return
			}

			// Reset the ConnectThread because we're done
			synchronized(this@ClientBluetoothService) {
				connectThread = null
			}
			// Start the connected thread
			connected(socket, device, socketType)
		}

		fun cancel() {
			try {
				socket.close()
			} catch (e: IOException) {
				Log.e(TAG, "close() of connect $socketType socket failed", e)
			}
		}
	}

	/**
	 * This thread runs during a connection with a remote device.
	 * It handles all incoming and outgoing transmissions.
	 */
	private inner class ConnectedThread(private val mmSocket: BluetoothSocket, socketType: String) : Thread() {
		private lateinit var mmInStream: InputStream
		private lateinit var mmOutStream: OutputStream

		init {
			Log.d(TAG, "create ConnectedThread: $socketType")
			// Get the BluetoothSocket input and output streams
			try {
				mmInStream = mmSocket.inputStream
				mmOutStream = mmSocket.outputStream
			} catch (e: IOException) {
				Log.e(TAG, "temp sockets not created", e)
			}
		}

		override fun run() {
			Log.i(TAG, "BEGIN connectedThread")
			val buffer = ByteArray(1024)
			var bytes: Int

			// Keep listening to the InputStream while connected
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer)
					// Send the obtained bytes to the UI Activity
					handler.obtainMessage(MESSAGE_READ,
							bytes,
							-1,
							buffer
					).sendToTarget()
				} catch (e: IOException) {
					Log.e(TAG, "disconnected", e)
					lostConnectionTo(mmSocket.remoteDevice)
					// Start the service over to restart listening mode
					this@ClientBluetoothService.start()
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
				mmOutStream.write(buffer)

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
				mmSocket.close()
			} catch (e: IOException) {
				Log.e(TAG, "close() of connect socket failed", e)
			}
		}
	}

	companion object {
		private val TAG = ClientBluetoothService::class.java.simpleName

		const val STATE_NONE = 0       // we're doing nothing
		const val STATE_LISTEN = 1     // now listening for incoming connections
		const val STATE_CONNECTING = 2 // now initiating an outgoing connection
		const val STATE_CONNECTED = 3  // now connected to a remote device

		// Unique UUID for this application
		private val MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
		private val MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
	}
}
