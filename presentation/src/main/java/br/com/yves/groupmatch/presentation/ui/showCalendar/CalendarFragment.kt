package br.com.yves.groupmatch.presentation.ui.showCalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.factory.showCalendar.CalendarPresenterFactory
import br.com.yves.groupmatch.presentation.runOnBackground


class CalendarFragment : NavHostFragment(), ShowCalendarView, TimeSlotAdapter.ItemClickListener {

    var mAdapter: TimeSlotAdapter? = null
    private var presenter: ShowCalendarPresenter = CalendarPresenterFactory.create(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun showLoading() {
        activity?.runOnUiThread {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    //endregion

   override fun onItemClick(view: View, position: Int){}
}

//class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {
//
//    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId))
//
//    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
//        super.getItemOffsets(outRect, view, parent, state)
//        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
//    }
//}

