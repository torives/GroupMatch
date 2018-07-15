package br.com.yves.groupmatch.scenes.calendar

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.calendar_day_recyclerview_item.view.*


data class Day(val weekDay: String, val hours: Array<Int> = Array(24) {it})

class TimeSlotAdapter(private val days: Array<Day>): RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {
    var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotAdapter.TimeSlotViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.calendar_day_recyclerview_item, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return days.map { it.hours.count() }.reduce { acc, i -> acc + i }
    }

    override fun onBindViewHolder(holder: TimeSlotAdapter.TimeSlotViewHolder, position: Int) {
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