package br.com.yves.groupmatch.presentation.ui.groups.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.account.UserViewModel
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

class GroupMemberAdapter(
		private var members: List<UserViewModel>? = null,
		private val glide: RequestManager
) : RecyclerView.Adapter<GroupMemberAdapter.GroupMemberViewHolder>() {

	//region RecyclerView.Adapter
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(
				R.layout.group_details_member_item,
				parent,
				false
		)
		return GroupMemberViewHolder(view)
	}

	override fun getItemCount(): Int = members?.size ?: 0

	override fun onBindViewHolder(holder: GroupMemberViewHolder, position: Int) {
		getMemberAt(position)?.let { member ->
			holder.bind(member)
		}
	}
	//endregion

	fun updateMembers(members: List<UserViewModel>) {
		this.members = members
		notifyDataSetChanged()
	}

	private fun getMemberAt(position: Int): UserViewModel? {
		return members?.getOrNull(position)
	}

	inner class GroupMemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val memberName by lazy { itemView.findViewById<TextView>(R.id.group_details_memberItem_name) }
		private val memberProfileImage by lazy { itemView.findViewById<ImageView>(R.id.group_details_memberItem_profileImage) }

		fun bind(member: UserViewModel) {
			memberName.text = member.name
			glide.load(member.profileImageURL)
					.apply(RequestOptions.circleCropTransform())
					.into(memberProfileImage)
		}
	}
}