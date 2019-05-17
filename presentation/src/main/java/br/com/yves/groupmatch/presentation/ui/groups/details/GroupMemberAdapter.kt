package br.com.yves.groupmatch.presentation.ui.groups.details

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.presentation.ui.account.UserViewModel
import com.bumptech.glide.RequestManager
import kotlinx.android.extensions.LayoutContainer

class GroupMemberAdapter(
		private var members: List<UserViewModel>? = null,
		private val glide: RequestManager
) : RecyclerView.Adapter<GroupMemberAdapter.GroupMemberViewHolder>() {

	//region RecyclerView.Adapter
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberViewHolder {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getItemCount(): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onBindViewHolder(holder: GroupMemberViewHolder, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion

	inner class GroupMemberViewHolder(view: View): RecyclerView.ViewHolder(view), LayoutContainer {
		override val containerView: View?
			get() = itemView

		fun bind(member: UserViewModel) {

		}
	}


}