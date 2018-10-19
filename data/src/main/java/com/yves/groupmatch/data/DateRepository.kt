package com.yves.groupmatch.data

import java.time.LocalDateTime

interface DateRepository{
    fun getCurrentDate(): LocalDateTime
}