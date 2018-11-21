package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.fragment_bluetooth_client.*

class BluetoothClientFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_bluetooth_client, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		foundServersList.layoutManager = LinearLayoutManager(context)
		foundServersList.adapter = ServerListAdapter(listOf("Tretinha", "Tretosa", "Tretuda"))
		foundServersList.addItemDecoration(
			DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)

		val serverIntent = Intent(activity, DeviceListActivity::class.java)
		startActivityForResult(serverIntent, 1)
	}


	companion object {
		private const val SCAN_PERIOD = 5000L
	}
}
