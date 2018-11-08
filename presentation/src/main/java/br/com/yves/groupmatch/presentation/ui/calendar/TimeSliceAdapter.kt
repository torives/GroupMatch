package br.com.yves.groupmatch.presentation.ui.calendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.timeslot_view.view.*

class TimeSliceAdapter(var calendar: CalendarViewModel) :
	RecyclerView.Adapter<TimeSliceAdapter.TimeSlotViewHolder>() {
	private val totalColumns = calendar.days.size
	var listener: ItemClickListener? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
		val view = TimeSlotView(parent.context)
		return TimeSlotViewHolder(view)
	}

	override fun getItemCount(): Int {
		return calendar.days.asSequence().map { it.hours.count() }.reduce { acc, i -> acc + i }
	}

	override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
		val hour = getHourAt(position)

		hour?.let {
			holder.itemView.hourTextView.text = hour.label
			holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(hour.status.hexColor))
		} ?: run {
			Log.d(
				"TimeSliceAdapter",
				"Could not find a model to bind ViewHolder at position: $position"
			)
		}
	}

	private fun columnAt(position: Int): Int {
		val matrixPosition = position + 1
		val reminder = matrixPosition.rem(totalColumns)

		return if (reminder == 0) totalColumns else reminder
	}

	fun getHourAt(position: Int): HourViewModel? {

		fun rowAt(position: Int) = position / totalColumns + 1

		val rowIndex = rowAt(position) - 1
		val day = getDayAt(position)

		return day?.hours?.getOrNull(rowIndex)
	}

	fun getDayAt(position: Int): DayViewModel? {
		val index = columnAt(position) - 1

		return calendar.days.getOrNull(index)
	}

	interface ItemClickListener {
		fun onItemClick(view: View, position: Int)
	}

	inner class TimeSlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		init {
			itemView.cardView.setOnClickListener {
				listener?.onItemClick(
					itemView,
					adapterPosition
				)
			}
		}
	}
}

class TimeSlotView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
	LinearLayout(context, attrs) {
	init {
		View.inflate(context, R.layout.timeslot_view, this)
	}
}