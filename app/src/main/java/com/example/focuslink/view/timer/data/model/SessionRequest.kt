package com.example.focuslink.view.timer.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class SessionRequest(
    // Store date objects for internal use
    private val startTimeDate: Date,
    private val endTimeDate: Date,

    @SerializedName("type")
    val type: String,

    @SerializedName("wasInterrupted")
    val wasInterrupted: Boolean,

    @SerializedName("interruptedBy")
    val interruptedBy: List<String>
) {
    // These properties will be serialized to JSON
    @SerializedName("startTime")
    val startTime: String = formatDateForServer(startTimeDate)

    @SerializedName("endTime")
    val endTime: String = formatDateForServer(endTimeDate)

    companion object {
        // Format date in the server's expected format - RFC 3339/ISO 8601 without milliseconds
        private fun formatDateForServer(date: Date): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return dateFormat.format(date)
        }
    }
}