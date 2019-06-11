package br.com.yves.groupmatch.presentation.ui.group.create.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.group.create.UserViewModel
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer

class GroupMemberAdapter2(
		private var members: List<UserViewModel>? = null,
		private val glide: RequestManager
) : RecyclerView.Adapter<GroupMemberAdapter2.GroupMemberViewHolder>() {

	//region RecyclerView.Adapter
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(
				R.layout.item_user_detail,
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

	inner class GroupMemberViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
		override val containerView: View?
			get() = itemView

		private val memberName by lazy { itemView.findViewById<TextView>(R.id.item_group_name) }
		private val memberProfileImage by lazy { itemView.findViewById<ImageView>(R.id.item_group_image) }

		fun bind(member: UserViewModel) {
			memberName.text = member.name
			glide.load(member.profileImageURL)
					.apply(RequestOptions.circleCropTransform())
					.into(memberProfileImage)
		}
	}
}