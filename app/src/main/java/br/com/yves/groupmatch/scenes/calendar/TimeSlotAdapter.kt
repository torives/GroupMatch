package br.com.yves.groupmatch.scenes.calendar

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.timeslot_view.view.*


enum class ScheduleStatus(val hexColor: String) {
    Available("#b3d044"),
    Busy("#c9232d")
}
class Hour(val label: String, var status: ScheduleStatus)
class Day(val weekDay: String, val hours: Array<Hour> = Array(24) { Hour("$it"+"h", ScheduleStatus.Available)})


class TimeSlotAdapter(private val days: Array<Day>): RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {
    private val totalColumns = days.size
    private val rowsPerColumn = days.first().hours.size
    var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotAdapter.TimeSlotViewHolder {
        val view = TimeSlotView(parent.context)
        return TimeSlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return days.map { it.hours.count() }.reduce { acc, i -> acc + i }
        //return day.hours.count()
    }

    override fun onBindViewHolder(holder: TimeSlotAdapter.TimeSlotViewHolder, position: Int) {
        val hour = getHourAt(position)

        hour?.let {
            holder.itemView.hourTextView.text = hour.label
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(hour.status.hexColor))
        } ?: run {
            Log.d("TimeSlotAdapter", "Could not find a model to bind ViewHolder at position: $position")
        }
    }

    private fun getHourAt(position: Int): Hour? {
        fun columnAt(position: Int): Int? {
            for (currentColumn in 1..totalColumns) {
                if (position in 0..(rowsPerColumn*currentColumn - 1)) {
                    return currentColumn
                }
            }
            return null
        }

        fun hourIndexAt(column: Int, position: Int): Int {
            return if (column == 1) position
            else position - rowsPerColumn * (column - 1)
        }

        val column = columnAt(position)
        column?.let {
            val index = hourIndexAt(column, position)
            return days[column-1].hours[index]
        } ?: run {
            return null
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    inner class TimeSlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.cardView.setOnClickListener { listener?.onItemClick(itemView, adapterPosition) }
        }
    }
}

class TimeSlotView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    init { View.inflate(context, R.layout.timeslot_view, this) }
}