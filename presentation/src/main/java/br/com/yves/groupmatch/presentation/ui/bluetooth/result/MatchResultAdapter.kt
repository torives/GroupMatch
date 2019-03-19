package br.com.yves.groupmatch.presentation.ui.bluetooth.result


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.bluetooth.server.MatchResultViewModel

class MatchResultAdapter(
		private val result: MatchResultViewModel
) : RecyclerView.Adapter<MatchResultAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context)
				.inflate(R.layout.item_matchresult, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		result.matchResult.getOrNull(position)?.let {
			holder.bind(it)
		}
	}

	override fun getItemCount(): Int = result.matchResult.size

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val weekDay: TextView = view.findViewById(R.id.weekDay)
		private val timePeriod: TextView = view.findViewById(R.id.timePeriod)

		fun bind(viewModel: MatchResultViewModel.MatchResult) {
			weekDay.text = viewModel.weekDay
			timePeriod.text = viewModel.timePeriod
		}
	}
}
