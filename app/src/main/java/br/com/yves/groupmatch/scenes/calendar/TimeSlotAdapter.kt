package br.com.yves.groupmatch.scenes.calendar

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.calendar_day_recyclerview_item.view.*

class TimeSlotAdapter(context: Context, var colorCodes: List<Int>): RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotAdapter.TimeSlotViewHolder {
        val view = layoutInflater.inflate(R.layout.calendar_day_recyclerview_item, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return colorCodes.count()
    }

    override fun onBindViewHolder(holder: TimeSlotAdapter.TimeSlotViewHolder, position: Int) {
        val colorCode = colorCodes[position]
        holder.itemView.cardView.setCardBackgroundColor(colorCode)
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