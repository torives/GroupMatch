package br.com.yves.groupmatch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SearchDevicesFragment : Fragment() {

    companion object {
        fun newInstance() = SearchDevicesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_search_devices, container, false)
    }
}
