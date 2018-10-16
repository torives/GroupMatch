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

class Hour(val label: String,
           var status: ScheduleStatus
)

class Day(val weekDay: String,
          val hours: Array<Hour> =
        Array(24) {
            Hour("$it"+"h", ScheduleStatus.Available)
        }
)


class TimeSlotAdapter(private val days: Array<Day>) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {
    private val totalColumns = days.size
    var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotAdapter.TimeSlotViewHolder {
        val view = TimeSlotView(parent.context)
        return TimeSlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return days.map { it.hours.count() }.reduce { acc, i -> acc + i }
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

    fun getHourAt(position: Int): Hour? {

        fun columnAt(position: Int): Int {
            val matrixPosition = position+1
            val reminder = matrixPosition.rem(totalColumns)

            return if(reminder == 0) totalColumns else reminder
        }

        fun rowAt(position: Int) = position/totalColumns + 1

        val columnIndex = columnAt(position) - 1
        val rowIndex = rowAt(position) - 1

        return if (days.indices.contains(columnIndex) && days[columnIndex].hours.indices.contains(rowIndex))
            days[columnIndex].hours[rowIndex]
        else null
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