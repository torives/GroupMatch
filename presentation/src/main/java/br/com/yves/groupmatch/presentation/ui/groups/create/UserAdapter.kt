package br.com.yves.groupmatch.presentation.ui.groups.create

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.view.*
import java.io.Serializable

class UserAdapter(
		private val glide: RequestManager,
		private var items: List<UserViewModel> = listOf(),
		private val listener: Listener
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

	fun update(users: List<UserViewModel>) {
		items = users
		notifyDataSetChanged()
	}

	interface Listener {
		fun onUserSelected(user: UserViewModel)
	}

	inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
		override val containerView: View?
			get() = itemView

		fun bind(item: UserViewModel) {
			itemView.item_user_name.text = item.name
			itemView.item_user_email.text = item.email
			glide.load(item.profileImageURL)
					.apply(RequestOptions.circleCropTransform())
					.into(itemView.item_user_image)
			itemView.item_user_check.visibility = if (item.isSelected) VISIBLE else GONE

			itemView.setOnClickListener { listener.onUserSelected(item) }
		}
	}
}

data class NewGroupDetailsViewModel(val members: List<UserViewModel>): Serializable

data class UserViewModel(
		val name: String,
		val email: String,
		var isSelected: Boolean,
		val profileImageURL: String? = null
): Serializable