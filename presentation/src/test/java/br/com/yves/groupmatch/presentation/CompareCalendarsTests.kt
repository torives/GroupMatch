import br.com.yves.groupmatch.domain.compareCalendars.CompareCalendars
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendarFactory
import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendarFactory
import br.com.yves.groupmatch.domain.showCalendar.Calendar
import br.com.yves.groupmatch.presentation.factory.DateRepositoryFactory
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime

class CompareCalendarsTests {

	private val dateRepository = DateRepositoryFactory.create()
	private val createCalendar = CreateCalendarFactory.create(dateRepository)
	private val currentWeek = dateRepository.getCurrentWeek()
	private lateinit var emptyCalendar: Calendar

	@Before
	fun before() {
		emptyCalendar = createCalendar.with(currentWeek).execute()
	}

	@Test
	fun compareWithEmptyCalendars() {
		val calendar = BusyCalendarFactory.create(emptyCalendar)
		val compareCalendars = CompareCalendars(listOf(calendar), createCalendar)

		val result = compareCalendars.execute()

		result.calendar.forEach {
			assert(!it.isBusy)
		}
	}

	@Test
	fun compareEmptyCalendarWithBusy(){
		var date: LocalDateTime
		emptyCalendar.first().apply {
			isBusy = true
			date = this.date
		}
		val calendar = BusyCalendarFactory.create(emptyCalendar)
		val result = CompareCalendars(listOf(calendar), createCalendar).execute()

		val resultTimeSlot = result.calendar.first { it.date == date }
		assert(resultTimeSlot.isBusy)
	}

	@Test
	fun compareEmptyCalendarWithTwoBusy(){
		var date: LocalDateTime
		emptyCalendar.first().apply {
			isBusy = true
			date = this.date
		}
		val calendar = BusyCalendarFactory.create(emptyCalendar)
		val result = CompareCalendars(listOf(calendar), createCalendar).execute()

		val resultTimeSlot = result.calendar.first { it.date == date }
		assert(resultTimeSlot.isBusy)
	}
}