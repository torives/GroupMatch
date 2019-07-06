package br.com.yves.groupmatch.presentation.ui.groups

import android.util.Log
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
import br.com.yves.groupmatch.domain.loadCalendar.LoadCalendar
import br.com.yves.groupmatch.domain.match.Match
import br.com.yves.groupmatch.domain.match.MatchRepository
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.slots.TimeSlot
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.presentation.factory.CalendarRepositoryFactory
import br.com.yves.groupmatch.presentation.factory.showCalendar.LoadCalendarFactory
import br.com.yves.groupmatch.presentation.ui.bluetooth.server.MatchResultViewModel
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.threeten.bp.format.TextStyle
import java.lang.ref.WeakReference
import java.util.*

class GroupController(
        view: GroupView,
        private val presenter: GroupPresenter,
        private val groupRepository: GroupRepository,
        private val matchRepository: MatchRepository,
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
        groups?.firstOrNull { it.id == groupId }?.let { group ->
            when (group.match?.status) {
                Match.Status.FINISHED -> displayMatchResult(group.match?.result)
                Match.Status.ONGOING, Match.Status.CREATED -> { } //do nothing
                else -> shouldStartMatch(group)
            }
        }
    }

    private fun displayMatchResult(result: List<TimeSlot>?) {
        result?.let {
            val vm = generateResultViewModel(result)
            view?.navigateToMatchResult(MatchResultViewModel(vm))
        }
    }

    private fun shouldStartMatch(group: Group) {
        view?.displayDialog(
                "Iniciar novo Match",
                "Os dados da sua agenda serão compartilhados com o grupo. Deseja continuar?",
                onPositiveResponse = { startMatch(group) },
                onNegativeResponse = { }
        )
    }

    private fun startMatch(group: Group) {
        val creator: User = authenticationService.getLoggedInUser()!! //FIXME: force unwrap
        val calendar = LoadCalendarFactory.create(CalendarRepositoryFactory.create()).execute() //TODO: send only busy

        startMatch(group, creator, calendar)
    }

    private fun startMatch(group: Group, creator: User, localCalendar: Calendar) {
        matchRepository.startMatch(group, creator, localCalendar, object : MatchRepository.StartMatchCallback {
            override fun onSuccess() {
                fetchGroupsForCurrentUser()
            }

            override fun onFailure(error: Error) {
                Log.e(TAG, "Failed to start match", error)
            }
        })
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

    companion object {
        private val TAG = GroupController::class.java.simpleName
    }
}