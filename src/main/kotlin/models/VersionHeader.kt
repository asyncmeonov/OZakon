package models

import java.util.Calendar
import java.util.Date

data class VersionHeader(val version: Int = 0,
                         val modifiedDate: Date =  Calendar.getInstance().time,
                         val history: List<Date> = listOf())