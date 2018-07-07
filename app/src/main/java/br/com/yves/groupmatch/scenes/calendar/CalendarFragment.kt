package br.com.yves.groupmatch.scenes.calendar

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment: NavHostFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DaysAdapter(context!!, arrayOf("treta", "tretinha", "tretosa"))
        daysRecyclerView.layoutManager = GridLayoutManager(context!!, 3)
        daysRecyclerView.adapter = adapter
    }

}

class DaysAdapter(context: Context, private var data: Array<String>): RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    var listener: AdapterView.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysAdapter.DayViewHolder {
        val view = layoutInflater.inflate(R.layout.calendar_day_recyclerview_item, parent, false)
        return DayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: DaysAdapter.DayViewHolder, position: Int) {
        holder.textView.text = data[position]
    }

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById<TextView>(R.id.info_text)
        init {
           // view.setOnClickListener { listener?. }
        }
    }
}
