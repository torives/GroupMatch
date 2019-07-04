package br.com.yves.groupmatch.presentation.ui.groups

import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
import br.com.yves.groupmatch.domain.match.Match
import br.com.yves.groupmatch.domain.models.slots.TimeSlot
import br.com.yves.groupmatch.presentation.ui.bluetooth.server.MatchResultViewModel
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.threeten.bp.format.TextStyle
import java.lang.Error
import java.lang.ref.WeakReference
import java.util.*

class GroupController(
		view: GroupView,
		private val presenter: GroupPresenter,
		private val groupRepository: GroupRepository,
		private val authenticationService: AuthenticationService
) {
	private var groups: List<Group>? = null
	private val viewWeakReference = WeakReference(view)
	private val view: GroupView?
		get() = viewWeakReference.get()

	fun onViewCreated() {}

	fun onViewResumed() {
		fetchGroupsForCurrentUser()
	}

	fun onGroupSelected(groupId: String) {
		val group = groups?.firstOrNull { it.id == groupId }

		if(group?.match?.status == Match.Status.FINISHED) {
			group.match?.result?.let {
				val vm = generateResultViewModel(it)
				view?.navigateToMatchResult(MatchResultViewModel(vm))
			}
		}
	}

	private fun generateResultViewModel(result: List<TimeSlot>): List<MatchResultViewModel.MatchResult> {
		return result.sortedByDescending { it.duration }
				.map {
					MatchResultViewModel.MatchResult(
							it.start.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
							"${it.start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))} - ${it.end.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))}"
					)
				}
	}

	fun onGroupCreationAttempt() {
		view?.navigateToNewGroup()
	}

	private fun fetchGroupsForCurrentUser() {
		authenticationService.getLoggedInUser()?.id?.let {
			groupRepository.getAllGroups(it, object : GroupRepository.GetAllGroupsCallback {
				override fun onSuccess(groups: List<Group>) {
					this@GroupController.groups = groups
					val viewModels = presenter.format(groups)
					view?.displayGroups(viewModels)
				}

				override fun onFailure(error: Error) {}
			})
		} ?: view?.displayLoggedOutLayout()
	}
}