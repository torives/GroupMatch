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
import java.util.UUID

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
(private val mHandler: Handler) {

	// Member fields
	private val mAdapter: BluetoothAdapter
	private var mConnectThread: ConnectThread? = null
	private var mConnectedThread: ConnectedThread? = null
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

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {
			mConnectThread!!.cancel()
			mConnectThread = null
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread!!.cancel()
			mConnectedThread = null
		}

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
			if (mConnectThread != null) {
				mConnectThread!!.cancel()
				mConnectThread = null
			}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread!!.cancel()
			mConnectedThread = null
		}

		// Start the thread to connect with the given device
		mConnectThread = ConnectThread(device, secure)
		mConnectThread!!.start()
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

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread!!.cancel()
			mConnectThread = null
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread!!.cancel()
			mConnectedThread = null
		}

		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = ConnectedThread(socket, socketType)
		mConnectedThread!!.start()

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

		if (mConnectThread != null) {
			mConnectThread!!.cancel()
			mConnectThread = null
		}

		if (mConnectedThread != null) {
			mConnectedThread!!.cancel()
			mConnectedThread = null
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
		val r: ConnectedThread?
		// Synchronize a copy of the ConnectedThread
		synchronized(this) {
			if (mState != STATE_CONNECTED) return
			r = mConnectedThread
		}
		// Perform the write unsynchronized
		r!!.write(out)
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private fun connectionFailed() {
		// Send a failure message back to the Activity
		val msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST)
		val bundle = Bundle()
		bundle.putString(Constants.TOAST, "Unable to connect device")
		msg.data = bundle
		mHandler.sendMessage(msg)

		// Start the service over to restart listening mode
		this@ClientBluetoothService.start()
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
		this@ClientBluetoothService.start()
	}

	/**
	 * This thread runs while attempting to make an outgoing connection
	 * with a device. It runs straight through; the connection either
	 * succeeds or fails.
	 */
	private inner class ConnectThread(private val mmDevice: BluetoothDevice, secure: Boolean) : Thread() {
		private val mmSocket: BluetoothSocket?
		private val mSocketType: String

		init {
			var tmp: BluetoothSocket? = null
			mSocketType = if (secure) "Secure" else "Insecure"

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				if (secure) {
					tmp = mmDevice.createRfcommSocketToServiceRecord(
							MY_UUID_SECURE)
				} else {
					tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(
							MY_UUID_INSECURE)
				}
			} catch (e: IOException) {
				Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e)
			}

			mmSocket = tmp
		}

		override fun run() {
			Log.i(TAG, "BEGIN mConnectThread SocketType:$mSocketType")
			name = "ConnectThread$mSocketType"

			// Always cancel discovery because it will slow down a connection
			mAdapter.cancelDiscovery()

			// Make a connection to the BluetoothSocket
			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				mmSocket!!.connect()
			} catch (e: IOException) {
				// Close the socket
				try {
					mmSocket!!.close()
				} catch (e2: IOException) {
					Log.e(TAG, "unable to close() " + mSocketType +
							" socket during connection failure", e2)
				}

				connectionFailed()
				return
			}

			// Reset the ConnectThread because we're done
			synchronized(this@ClientBluetoothService) {
				mConnectThread = null
			}

			// Start the connected thread
			connected(mmSocket, mmDevice, mSocketType)
		}

		fun cancel() {
			try {
				mmSocket!!.close()
			} catch (e: IOException) {
				Log.e(TAG, "close() of connect $mSocketType socket failed", e)
			}

		}
	}

	/**
	 * This thread runs during a connection with a remote device.
	 * It handles all incoming and outgoing transmissions.
	 */
	private inner class ConnectedThread(private val mmSocket: BluetoothSocket, socketType: String) : Thread() {
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
