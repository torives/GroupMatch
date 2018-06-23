package br.com.yves.groupmatch.scenes.connectionRole

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.R.id.clientButton
import br.com.yves.groupmatch.R.id.serverButton
import kotlinx.android.synthetic.main.fragment_connection_role.*
import kotlinx.android.synthetic.main.fragment_connection_role.view.*
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.support.v4.toast

class ConnectionRoleFragment: Fragment() {

    private var mBluetoothManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_connection_role, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBluetoothManager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        mBluetoothAdapter = mBluetoothManager?.adapter

        setupButtonsOnClickListener()
    }

    private fun setupButtonsOnClickListener() {

        serverButton.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_connectionRoleFragment_to_searchBluetoothClientsFragment)
        }
        clientButton.setOnClickListener { toast("client") }
    }

    override fun onResume() {
        super.onResume()

        // Check if bluetooth is enabled
        if (mBluetoothAdapter?.isEnabled == false) {
            // Request user to enable it
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBtIntent)
            return
        }

        // Check low energy support
        if (activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) == false) {
            // Get a newer device
//            log("No LE Support.")
            activity?.finish()
            return
        }

        // Check advertising
        if (mBluetoothAdapter?.isMultipleAdvertisementSupported == false) {
            // Unable to run the server on this device, get a better device
//            log("No Advertising Support.")
            activity?.finish()
            return
        }
    }
}