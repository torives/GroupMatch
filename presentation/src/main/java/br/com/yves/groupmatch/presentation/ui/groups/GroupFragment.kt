package br.com.yves.groupmatch.presentation.ui.groups


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.auth.GroupMatchAuth
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.models.account.User
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_groups.*
import org.threeten.bp.LocalDateTime

interface GroupsView {
	fun displayGroups(groups: List<GroupViewModel>)
	fun displayLoggedOutLayout()
}

data class Group(
		val name: String,
		val thumbnailURL: String,
		val members: List<User>,
		val admin: User,
		val lastInteractionDate: LocalDateTime
)

interface GroupRepository {
	fun getAllGroups(uid: String): List<Group>
}

class GroupMockRepository : GroupRepository {
	override fun getAllGroups(uid: String): List<Group> {
		val members = listOf(
				User("", "Luizinho", ""),
				User("", "Zezinho", ""),
				User("", "Betinho", ""),
				User("", "Boninho", ""),
				User("", "Luluzinha", ""),
				User("", "Magali", "")
		)
		return listOf(
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now())
		)
	}
}

interface GroupPresenter {
	fun format(groups: List<Group>): List<GroupViewModel>
}

class GroupPresenterImpl : GroupPresenter {
	override fun format(groups: List<Group>): List<GroupViewModel> {
		return groups.map { group ->
			val members: String = group.members.map { it.name }.reduce { acc, s ->
				if (acc.isEmpty()) {
					s
				} else {
					"$acc, $s"
				}
			}
			GroupViewModel(group.name, members, group.thumbnailURL, false, "")
		}
	}
}

class GroupController(
		private val view: GroupsView,
		private val presenter: GroupPresenter,
		private val groupRepository: GroupRepository,
		private val authenticationService: AuthenticationService
) {

	fun onViewCreated() {
		authenticationService.getUser()?.uid?.let {
			val groups = groupRepository.getAllGroups(it)
			val viewModels = presenter.format(groups)

			view.displayGroups(viewModels)
		} ?: view.displayLoggedOutLayout()
	}

	fun onGroupSelected() {}

	fun onGroupCreationAttempt() {}
}

class GroupFragment : Fragment(), GroupsView {

	private val controller = GroupController(
			this,
			GroupPresenterImpl(),
			GroupMockRepository(),
			GroupMatchAuth.instance
	)
	private lateinit var groupAdapter: GroupAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_groups, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		groupAdapter = GroupAdapter(null, Glide.with(this))
		recyclerview_groups_list.adapter = groupAdapter
		recyclerview_groups_list.layoutManager = LinearLayoutManager(context)
		recyclerview_groups_list.setHasFixedSize(true)
		recyclerview_groups_list.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)

		runOnBackground {
			controller.onViewCreated()
		}
	}

	//region GroupsView
	override fun displayGroups(groups: List<GroupViewModel>) = runOnUiThread {
		groupAdapter.updateGroups(groups)
	}

	override fun displayLoggedOutLayout() = runOnUiThread {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion
}
