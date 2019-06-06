package br.com.yves.groupmatch.presentation.ui.groups.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import kotlinx.android.extensions.LayoutContainer

class UserAdapter(
		private var items: List<UserViewModel> = listOf()
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

	//region RecyclerView.Adapter
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
		return UserViewHolder(view)
	}

	override fun getItemCount(): Int {
		return items.size
	}

	override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
		getUserAt(position)?.let {
			holder.bind(it)
		}
	}
	//endregion

	private fun getUserAt(position: Int): UserViewModel? {
		return items.getOrNull(position)
	}

	inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
		override val containerView: View?
			get() = itemView

		fun bind(item: UserViewModel) {

		}
	}
}

class UserViewModel(
		val name: String,
		val email: String,
		var isSelected: Boolean,
		val profileImageURL: String? = null
)