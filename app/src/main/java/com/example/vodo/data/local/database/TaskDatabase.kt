package com.example.vodo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vodo.data.local.dao.TaskDao
import com.example.vodo.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase:RoomDatabase() {

    public abstract val dao:TaskDao

}