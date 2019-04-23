package br.com.yves.groupmatch.presentation.ui.groups


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.item_group.view.*

data class GroupViewModel(
		val name: String,
		val members: String,
		val imageURL: String,
		val hasUpdates: Boolean,
		val lasInteractionDate: String
)

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
				itemView.item_group_notificationIcon.visibility = if (hasUpdates) VISIBLE else GONE
			}

			glide.load(viewModel.imageURL)
					.apply(RequestOptions.circleCropTransform())
					.into(itemView.item_group_image)
		}
	}
}

class GroupsFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_groups, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val groups = listOf(
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19")

		)
		val groupAdapter = GroupAdapter(groups, Glide.with(this))
		recyclerview_groups_list.layoutManager = LinearLayoutManager(context)
		recyclerview_groups_list.setHasFixedSize(true)
		recyclerview_groups_list.adapter = groupAdapter
	}
}
