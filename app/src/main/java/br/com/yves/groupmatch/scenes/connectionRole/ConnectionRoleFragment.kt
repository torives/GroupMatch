package br.com.yves.groupmatch.scenes.connectionRole

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.R.id.clientButton
import br.com.yves.groupmatch.R.id.serverButton
import kotlinx.android.synthetic.main.fragment_connection_role.*
import org.jetbrains.anko.support.v4.toast

interface ConnectionRoleView {
    fun getBluetoothManager(): BluetoothManager?
    fun getPackageManager(): PackageManager?
}

class ConnectionRoleFragment: Fragment(), ConnectionRoleView {

    private var mInteractor: ConnectionRoleInteractor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mInteractor = ConnectionRoleInteractor(this)
        lifecycle.addObserver(mInteractor!!)
        return inflater.inflate(R.layout.fragment_connection_role, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonsOnClickListener()
    }


    private fun setupButtonsOnClickListener() {
        serverButton.setOnClickListener { toast("server") }
        clientButton.setOnClickListener { toast("client") }
    }


    /*
    *    ConnectionRoleView Implementation
    */

    override fun getBluetoothManager(): BluetoothManager? = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    override fun getPackageManager(): PackageManager? = activity?.packageManager

}