package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R

class ServerListAdapter(private val servers: MutableList<String> = mutableListOf()) :
	RecyclerView.Adapter<ServerListAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(
			R.layout.item_server_list,
			parent,
			false
		)
		return ViewHolder(view)
	}

	override fun getItemCount() = servers.count()

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		servers.getOrNull(position)?.let {
			holder.bind(it)
		}
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val serverName: TextView by lazy {
			itemView.findViewById<TextView>(
				R.id.serverName
			)
		}

		fun bind(name: String) {
			serverName.text = name
		}
	}

	fun add(device: String){
		servers.add(device)
		notifyItemChanged(servers.lastIndex)
	}

	fun clear() {
		servers.clear()
		notifyDataSetChanged()
	}
}