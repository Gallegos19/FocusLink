package com.example.focuslink.core.data.local.session.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session")
data class SessionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val startTime: Long,
    val endTime: Long,
    val type: String, // "focus" | "break" | "long_break"
    val wasInterrupted: Boolean,
    val interruptedBy: List<String>
)
