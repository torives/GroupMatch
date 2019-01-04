package br.com.yves.groupmatch.domain

import org.threeten.bp.LocalDateTime

data class Week(override val start: LocalDateTime, override val endInclusive: LocalDateTime): ClosedRange<LocalDateTime>