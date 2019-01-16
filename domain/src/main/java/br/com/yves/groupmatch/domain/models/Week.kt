package br.com.yves.groupmatch.domain.models

import org.threeten.bp.LocalDateTime

open class Week(
		override val start: LocalDateTime,
		open val end: LocalDateTime
) : ClosedRange<LocalDateTime> {
	override val endInclusive = end
}