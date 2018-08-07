package br.com.yves.groupmatch.scenes.calendar

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.DimenRes
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.time.DayOfWeek
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_calendar.view.*


class CalendarFragment: NavHostFragment(), TimeSlotAdapter.ItemClickListener {
    var mAdapter: TimeSlotAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val days = arrayOf(
                Day("Seg"),
                Day("Ter"),
                Day("Qua"),
                Day("Qui"),
                Day("Sex"),
                Day("Sab"),
                Day("Dom")
        )

        mAdapter = TimeSlotAdapter(days)
        mAdapter?.listener = this

        daysRecyclerView.addItemDecoration(ItemOffsetDecoration(context!!, R.dimen.time_slot_item_spacing))
        daysRecyclerView.adapter = mAdapter
        (daysRecyclerView.layoutManager as GridLayoutManager).spanCount = days.count()
    }

    override fun onItemClick(view: View, position: Int) {
        val hour = mAdapter?.getHourAt(position)
        hour?.let { toggleStatus(it) }
        mAdapter?.notifyItemChanged(position)
    }

    private fun toggleStatus(hour: Hour) {
        hour.status = when(hour.status) {
            ScheduleStatus.Available -> ScheduleStatus.Busy
            ScheduleStatus.Busy -> ScheduleStatus.Available
        }
    }
}

class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId))

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}