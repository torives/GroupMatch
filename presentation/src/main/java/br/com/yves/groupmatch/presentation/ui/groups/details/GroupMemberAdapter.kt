package br.com.yves.groupmatch.presentation.ui.groups.details

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.presentation.ui.account.UserViewModel
import com.bumptech.glide.RequestManager

class GroupMemberAdapter(
		private var members: List<UserViewModel>? = null,
		private val glide: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	//region RecyclerView.Adapter
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getItemCount(): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion

}