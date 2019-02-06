package br.com.yves.groupmatch.presentation.ui.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import br.com.yves.groupmatch.R

class TimeSlotView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
		LinearLayout(context, attrs) {
	init {
		View.inflate(context, R.layout.timeslot_view, this)
	}
}