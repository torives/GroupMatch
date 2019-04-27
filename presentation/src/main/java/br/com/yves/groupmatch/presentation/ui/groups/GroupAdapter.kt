package br.com.yves.groupmatch.presentation.ui.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_group.view.*

class GroupAdapter(
		private val groups: List<GroupViewModel>,
		private val glide: RequestManager
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(
				R.layout.item_group,
				parent,
				false
		)
		return GroupViewHolder(view)
	}

	override fun getItemCount() = groups.size

	override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
		groupAt(position)?.let {
			holder.bind(it)
		}
	}

	private fun groupAt(position: Int): GroupViewModel? {
		return groups.getOrNull(position)
	}

	inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(viewModel: GroupViewModel) {
			viewModel.apply {
				itemView.item_group_name.text = name
				itemView.item_group_members.text = members
				itemView.item_group_lastInteractionDate.text = lasInteractionDate
				itemView.item_group_notificationIcon.visibility = if (hasUpdates) View.VISIBLE else View.GONE
			}

			glide.load(viewModel.imageURL)
					.apply(RequestOptions.circleCropTransform())
					.into(itemView.item_group_image)
		}
	}
}