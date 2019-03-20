package br.com.yves.groupmatch.presentation.ui.calendar

data class TimeSlotViewModel(
		val dayAndMonth: String,
		val weekDay: String,
		val hour: String,
		var status: Status
) {
	enum class Status(val hexColor: String) {
		Available("#b3d044"),
		Busy("#c9232d")
	}
}