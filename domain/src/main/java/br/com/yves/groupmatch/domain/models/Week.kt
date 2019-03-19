package br.com.yves.groupmatch.domain.models

import org.threeten.bp.LocalDateTime

open class Week(
		override val start: LocalDateTime,
		open val end: LocalDateTime
) : ClosedRange<LocalDateTime> {
	override var endInclusive = end

	override fun equals(other: Any?): Boolean {
		val otherWeek = other as? Week
		return if(otherWeek == null) {
			false
		} else {
			this.start == otherWeek.start && this.end == otherWeek.end
		}
	}
}