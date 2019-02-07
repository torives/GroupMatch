package br.com.yves.groupmatch.presentation.ui.calendar

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.timeslot_view.view.*

class TimeSlotAdapter(var calendar: CalendarViewModel) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {
	private val columns = calendar.daysCount
	private val rows = calendar.timeslots.size/columns
	var listener: OnItemClickListener? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
		val view = TimeSlotView(parent.context)
		return TimeSlotViewHolder(view)
	}

	override fun getItemCount(): Int {
		return calendar.timeslots.size
	}

	override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
		getTimeSlotAt(position)?.apply {
			holder.itemView.hourTextView.text = hour
			holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(status.hexColor))
		} ?: Log.d(TAG, "Could not find a model to bind ViewHolder at position: $position")
	}

	fun getTimeSlotAt(position: Int): TimeSlotViewModel? {

		fun rowAt(position: Int): Int {
			val matrixPosition = position + 1
			val reminder = matrixPosition.rem(columns)

			return if (reminder == 0) columns else reminder
		}

		fun columnAt(position: Int) = position / columns + 1

		fun matrixToIndex(row: Int, column: Int): Int {
			return row*(rows) + (column)
		}

        val columnIndex = columnAt(position) - 1
        val rowIndex = rowAt(position) - 1
        val index = matrixToIndex(rowIndex, columnIndex)
		return calendar.timeslots.getOrNull(index)
	}

	interface OnItemClickListener {
		fun onItemClick(calendarId: Long, position: Int)
	}

	inner class TimeSlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		init {
			itemView.cardView.setOnClickListener {
				listener?.onItemClick(
						calendar.id,
						adapterPosition
				)
			}
		}
	}

	companion object {
		private val TAG = TimeSlotAdapter::class.java.simpleName
	}
}