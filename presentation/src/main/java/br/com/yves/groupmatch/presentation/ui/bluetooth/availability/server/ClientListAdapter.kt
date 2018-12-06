package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.BluetoothClient

class ClientListAdapter(val onItemClick: ((BluetoothClient) -> Unit)? = null) :
		RecyclerView.Adapter<ClientListAdapter.ViewHolder>() {

	private val clients = linkedSetOf<BluetoothClient>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(
				R.layout.item_server_list, //FIXME: criar layout próprio
				parent,
				false
		)
		return ViewHolder(view)
	}

	override fun getItemCount() = clients.count()

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		clients.elementAtOrNull(position)?.let {
			holder.bind(it)
		}
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val clientName: TextView by lazy {
			itemView.findViewById<TextView>(
					R.id.serverName
			)
		}

		fun bind(client: BluetoothClient) {
			clientName.text = client.name
			clientName.setOnClickListener { onItemClick?.invoke(client) }
		}
	}

	fun add(server: BluetoothClient){
		clients.add(server)
		notifyItemChanged(clients.indices.last)
	}

	fun addAll(servers: Collection<BluetoothClient>){
		val lastPosition = this.clients.indices.last

		this.clients.addAll(servers)
		notifyItemRangeChanged(lastPosition, servers.size)
	}

	fun clear() {
		clients.clear()
		notifyDataSetChanged()
	}
}