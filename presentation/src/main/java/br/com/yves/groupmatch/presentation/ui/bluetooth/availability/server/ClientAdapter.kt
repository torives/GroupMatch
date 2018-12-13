package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.BluetoothClient

class ClientAdapter(val onItemClick: ((BluetoothClient) -> Unit)? = null) :
		RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

	private val clients = mutableListOf<BluetoothClient>()

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

	//TODO: add status indicators
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
		if(!clients.contains(server)){
			clients.add(server)
			notifyItemChanged(clients.lastIndex)
		}
	}

	fun addAll(servers: Collection<BluetoothClient>){
		val lastPosition = this.clients.lastIndex

		this.clients.addAll(servers)
		notifyItemRangeChanged(lastPosition, servers.size)
	}

	fun update(client: BluetoothClient) {
		val index = clients.indexOf(client)
		if(index < 0) {
			throw IllegalStateException("Trying to update client $client before adding it to the list")
		} else {
			clients[index] = client
			notifyItemChanged(index)
		}
	}

	fun remove(client: BluetoothClient) {
		val index = clients.indexOfFirst { it.name == client.name }
		if(index < 0) {
			throw IllegalStateException("Attempt to remove client $client who is not on the list")
		} else {
			clients.removeAt(index)
			notifyItemChanged(index)
		}
	}

	fun clear() {
		clients.clear()
		notifyDataSetChanged()
	}
}