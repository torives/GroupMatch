package br.com.yves.groupmatch.presentation.ui.calendar

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.factory.showCalendar.CalendarPresenterFactory
import br.com.yves.groupmatch.presentation.runOnBackground
import kotlinx.android.synthetic.main.fragment_calendar.*


class CalendarFragment : NavHostFragment(), CalendarView, TimeSliceAdapter.ItemClickListener {

	private var presenter: CalendarPresenter = CalendarPresenterFactory.create(this)

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_calendar, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		runOnBackground {
			presenter.onViewCreated()
		}
	}

	override fun showCalendar(calendar: CalendarViewModel) {
		activity?.runOnUiThread {

			var adapter = daysRecyclerView?.adapter as? TimeSliceAdapter
			adapter?.apply {
				this.calendar = calendar
				notifyDataSetChanged()
			} ?: run {
				adapter = TimeSliceAdapter(calendar)
				adapter?.listener = this
				daysRecyclerView?.adapter = adapter

				daysRecyclerView?.addItemDecoration(
					ItemOffsetDecoration(
						context!!,
						R.dimen.time_slot_item_spacing
					)
				)
				(daysRecyclerView?.layoutManager as GridLayoutManager).spanCount =
						calendar.days.count()

			}
		}
	}

	override fun showLoading() {
		activity?.runOnUiThread {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}
	}
	//endregion

	override fun onItemClick(view: View, position: Int) {
		val adapter = daysRecyclerView.adapter as? TimeSliceAdapter
		val day = adapter?.getDayAt(position)
		val hour = adapter?.getHourAt(position)

		if (day != null && hour != null) {
			runOnBackground {
				presenter.onTimeSlotClicked(day, hour)
			}
		}
	}
}

class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

	constructor(context: Context, @DimenRes itemOffsetId: Int) : this(
		context.resources.getDimensionPixelSize(
			itemOffsetId
		)
	)

	override fun getItemOffsets(
		outRect: Rect,
		view: View,
		parent: RecyclerView,
		state: RecyclerView.State
	) {
		super.getItemOffsets(outRect, view, parent, state)
		outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
	}
}

