import br.com.yves.groupmatch.domain.compareCalendars.CompareCalendars
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendarFactory
import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendarFactory
import br.com.yves.groupmatch.domain.showCalendar.Calendar
import br.com.yves.groupmatch.presentation.factory.DateRepositoryFactory
import org.junit.Before
import org.junit.Test

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
	fun compareTwoEmptyCalendars() {
		val calendar = BusyCalendarFactory.create(emptyCalendar)
		val compareCalendars = CompareCalendars(listOf(calendar, calendar), createCalendar)

		val result = compareCalendars.execute()

		val busyIndex = result.calendar.firstOrNull { it.isBusy }
		assert(busyIndex == null)
	}

	@Test
	fun compareFreeTimeSlotWithBusyTimeSlot() {
		val busySlotCalendar = emptyCalendar.subList(0, emptyCalendar.lastIndex)
		busySlotCalendar.first().apply {
			isBusy = true
		}

		val freeBusyCalendar = BusyCalendarFactory.create(emptyCalendar)
		val busyBusyCalendar = BusyCalendarFactory.create(busySlotCalendar)
		val result = CompareCalendars(listOf(freeBusyCalendar, busyBusyCalendar), createCalendar).execute()

		val resultTimeSlot = result.calendar.first()
		assert(resultTimeSlot.isBusy)
	}

	@Test
	fun compareFreeTimeSlotWithTwoBusyTimeSlot() {
		val busySlotCalendar = emptyCalendar.subList(0, emptyCalendar.size)
		busySlotCalendar.first().apply {
			isBusy = true
		}

		val freeBusyCalendar = BusyCalendarFactory.create(emptyCalendar)
		val busyBusyCalendar = BusyCalendarFactory.create(busySlotCalendar)
		val result = CompareCalendars(
				listOf(freeBusyCalendar, busyBusyCalendar, busyBusyCalendar),
				createCalendar
		).execute()

		val resultTimeSlot = result.calendar.first()
		assert(resultTimeSlot.isBusy)
	}
}