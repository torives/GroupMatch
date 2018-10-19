package com.yves.groupmatch.data

import com.example.domain.Event

interface GetEventsFromCurrentWeekCallback {
    fun onGetEventsFromCurrentWeekSuccess(events: List<com.example.domain.Event>)
}