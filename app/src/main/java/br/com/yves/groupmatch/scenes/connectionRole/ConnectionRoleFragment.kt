package br.com.yves.groupmatch.scenes.connectionRole

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.fragment_connection_role.*
import org.jetbrains.anko.support.v4.toast

class ConnectionRoleFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
}