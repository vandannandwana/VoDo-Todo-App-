package com.example.vodo.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.vodo.core.contants.TASK_TABLE_NAME
import com.example.vodo.data.local.entity.TaskEntity

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task:TaskEntity)

    @Delete
    suspend fun deleteTask(task:TaskEntity)

    @Query("SELECT * FROM $TASK_TABLE_NAME ORDER BY completed ASC ")
    fun getTasksOrderByTimeAsc(): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM $TASK_TABLE_NAME WHERE title LIKE :searchQuery ORDER BY completed ASC ")
    fun getSearchedTasks(searchQuery:String): LiveData<List<TaskEntity>>



}