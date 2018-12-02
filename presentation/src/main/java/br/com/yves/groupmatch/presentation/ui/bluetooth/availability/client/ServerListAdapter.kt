package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R

class ServerListAdapter(val onItemClick: ((BluetoothServer) -> Unit)? = null) :
	RecyclerView.Adapter<ServerListAdapter.ViewHolder>() {

	private val servers = linkedSetOf<BluetoothServer>()

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
		private val serverName: TextView by lazy {
			itemView.findViewById<TextView>(
				R.id.serverName
			)
		}
		private val serverAddress: TextView by lazy {
			itemView.findViewById<TextView>(R.id.serverAddress)
		}

		fun bind(server: BluetoothServer) {
			serverName.text = server.name
			serverAddress.text = server.address
			itemView.setOnClickListener { onItemClick?.invoke(server) }
		}
	}

	fun add(server: BluetoothServer){
		servers.add(server)
		notifyItemChanged(servers.indices.last)
	}

	fun addAll(servers: Collection<BluetoothServer>){
		val lastPosition = this.servers.indices.last

		this.servers.addAll(servers)
		notifyItemRangeChanged(lastPosition, servers.size)
	}

	fun clear() {
		servers.clear()
		notifyDataSetChanged()
	}
}