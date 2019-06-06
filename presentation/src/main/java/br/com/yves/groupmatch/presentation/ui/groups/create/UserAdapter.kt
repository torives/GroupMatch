package br.com.yves.groupmatch.presentation.ui.groups.create

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserListViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getItemViewType(position: Int): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getItemCount(): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	abstract inner class UserListViewHolder(view: View): RecyclerView.ViewHolder(view), LayoutContainer {
		override val containerView: View?
			get() = itemView

		abstract fun bind(item: UserListItem)
	}
}


sealed class UserListItem