package com.example.focuslink.core.data.local.user.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.focuslink.core.data.local.session.entities.SessionEntity
import com.example.focuslink.core.data.local.user.entities.UserEntity

data class UserWithSessions(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val sessions: List<SessionEntity>
)
