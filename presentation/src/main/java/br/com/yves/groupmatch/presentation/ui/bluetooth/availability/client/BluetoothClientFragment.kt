package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.yves.groupmatch.R

class BluetoothClientFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_bluetooth_client, container, false)
	}


	companion object {
		private const val SCAN_PERIOD = 5000L
	}
}
