package br.com.yves.groupmatch.debug


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.calendar.ItemOffsetDecoration
import br.com.yves.groupmatch.presentation.ui.groups.details.GridAutofitLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_new_group_details.*

data class GroupMemberViewModel(
		val name: String,
		val profileImageURL: String
)

class GroupMemberAdapter2(
		private var members: List<GroupMemberViewModel>? = null,
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

	fun updateMembers(members: List<GroupMemberViewModel>) {
		this.members = members
		notifyDataSetChanged()
	}

	private fun getMemberAt(position: Int): GroupMemberViewModel? {
		return members?.getOrNull(position)
	}

	inner class GroupMemberViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
		override val containerView: View?
			get() = itemView

		private val memberName by lazy { itemView.findViewById<TextView>(R.id.item_group_name) }
		private val memberProfileImage by lazy { itemView.findViewById<ImageView>(R.id.item_group_image) }

		fun bind(member: GroupMemberViewModel) {
			memberName.text = member.name
			glide.load(member.profileImageURL)
					.apply(RequestOptions.circleCropTransform())
					.into(memberProfileImage)
		}
	}
}

class NewGroupDetails : Fragment() {

	private lateinit var adapter: GroupMemberAdapter2
	//region Lifecycle
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_new_group_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		(activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.new_group_details_toolbarSubtitle)

		adapter = GroupMemberAdapter2(listOf(
				GroupMemberViewModel("Fulaninho de Tal", "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				GroupMemberViewModel("Fulaninho de Tal", "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				GroupMemberViewModel("Fulaninho de Tal", "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				GroupMemberViewModel("Fulaninho de Tal", "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				GroupMemberViewModel("Fulaninho de Tal", "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				GroupMemberViewModel("Fulaninho de Tal", "https://i.redd.it/4fz0ct0l7mo11.jpg")
		), Glide.with(this))

		new_group_details_membersRecyclerView.layoutManager = GridAutofitLayoutManager(context!!, 200)
		new_group_details_membersRecyclerView.adapter = adapter
//		new_group_details_membersRecyclerView.addItemDecoration(
//				ItemOffsetDecoration(context!!, R.dimen.time_slot_item_spacing)
//		)
	}
	//endregion
}
