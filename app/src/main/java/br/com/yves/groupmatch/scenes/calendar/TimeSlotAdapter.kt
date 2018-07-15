package br.com.yves.groupmatch.scenes.calendar

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.timeslot_view.view.*


data class Day(val weekDay: String, val hours: Array<Int> = Array(24) {it})

class TimeSlotAdapter(private val days: Array<Day>): RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {
    var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotAdapter.TimeSlotViewHolder {
        val view = TimeSlotView(parent.context)
        return TimeSlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return days.map { it.hours.count() }.reduce { acc, i -> acc + i }
    }

    override fun onBindViewHolder(holder: TimeSlotAdapter.TimeSlotViewHolder, position: Int) {
        holder.itemView.hourTextView.text = "00h"
        holder.itemView.cardView.setCardBackgroundColor(Color.parseColor("#B26EEF"))
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