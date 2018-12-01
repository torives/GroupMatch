package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R

class ServerListAdapter(val onItemClick: ((String) -> Unit)? = null) :
	RecyclerView.Adapter<ServerListAdapter.ViewHolder>() {

	private val servers = linkedSetOf<String>()

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
		servers.elementAtOrNull(position)?.let {
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
			serverName.setOnClickListener { onItemClick?.invoke(name) }
		}
	}

	fun add(device: String){
		servers.add(device)
		notifyItemChanged(servers.indices.last)
	}

	fun clear() {
		servers.clear()
		notifyDataSetChanged()
	}
}