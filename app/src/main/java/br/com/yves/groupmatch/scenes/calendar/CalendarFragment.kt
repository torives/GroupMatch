package br.com.yves.groupmatch.scenes.calendar

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.DimenRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import br.com.yves.groupmatch.R
import android.support.v7.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.WeekFields


class CalendarFragment : NavHostFragment(), CalendarView, TimeSlotAdapter.ItemClickListener {
    var mAdapter: TimeSlotAdapter? = null

    lateinit var viewModel: CalendarEventViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

   override fun onItemClick(view: View, position: Int){}
}

class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId))

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}

//TODO:
data class EventViewModel(val date: Day)

interface CalendarView {
    fun showEvents(events: List<EventViewModel>)
    fun showLoading()
}

interface CalendarPresenter {
    fun onViewCreated()
}

class GetEventsFromCurrentWeek(private val eventRepository: EventRepository, private val dateRepository: DateRepository) : UseCase<Unit>() {
    private lateinit var callback: GetEventsFromCurrentWeekCallback

    //TODO:
    override fun execute() {
        callback.onGetEventsFromCurrentWeekSuccess(listOf(Event()))
    }

    fun with(callback: GetEventsFromCurrentWeekCallback): GetEventsFromCurrentWeek {
        this.callback = callback
        return this
    }
}

interface EventRepository{
    fun getEventsAt(date: LocalDateTime): List<Event>
    fun getEventsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<Event>
}

interface DateRepository{
    fun getCurrentDate(): LocalDateTime
}

interface GetEventsFromCurrentWeekCallback {
    fun onGetEventsFromCurrentWeekSuccess(events: List<Event>)
}

class Event()

abstract class UseCase<out T> {
    abstract fun execute(): T
}