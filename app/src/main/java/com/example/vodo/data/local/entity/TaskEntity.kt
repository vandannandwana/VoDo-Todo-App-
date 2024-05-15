package com.example.vodo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vodo.core.contants.TASK_TABLE_NAME
import java.sql.Time

@Entity(tableName = TASK_TABLE_NAME)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val description: String,
    val completed: Boolean,
    val time: String
)
