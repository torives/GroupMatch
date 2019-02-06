package br.com.yves.groupmatch.presentation.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.factory.showCalendar.CalendarPresenterFactory
import br.com.yves.groupmatch.presentation.runOnBackground
import kotlinx.android.synthetic.main.fragment_calendar.*


class CalendarFragment : NavHostFragment(), CalendarView, TimeSlotAdapter.OnItemClickListener {

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

			var adapter = daysRecyclerView?.adapter as? TimeSlotAdapter
			adapter?.apply {
				this.calendar = calendar
				notifyDataSetChanged()
			} ?: run {
				adapter = TimeSlotAdapter(calendar)
				adapter?.listener = this
				daysRecyclerView?.adapter = adapter

				daysRecyclerView?.addItemDecoration(
						ItemOffsetDecoration(
								context!!,
								R.dimen.time_slot_item_spacing
						)
				)
				(daysRecyclerView?.layoutManager as? GridLayoutManager)?.spanCount =
						calendar.daysCount

			}
		}
	}

	override fun showLoading() {
		activity?.runOnUiThread {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}
	}
	//endregion

	override fun onItemClick(calendarId: Long, position: Int) {
		val adapter = daysRecyclerView.adapter as? TimeSlotAdapter
		adapter?.getTimeSlotAt(position)?.let {
			runOnBackground {
				presenter.onTimeSlotClicked(calendarId, it)
			}
		}
	}
}
