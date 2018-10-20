package br.com.yves.groupmatch.presentation.ui.loadEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.loadEvents.CalendarPresenterFactory


class CalendarFragment : NavHostFragment(), LoadEventsView, TimeSlotAdapter.ItemClickListener {

    var mAdapter: TimeSlotAdapter? = null
    lateinit var viewModel: CalendarEventViewModel
    private var presenter: CalendarPresenter = CalendarPresenterFactory.create(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    //region LoadEventsView
    override fun showEvents(events: List<EventViewModel>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

