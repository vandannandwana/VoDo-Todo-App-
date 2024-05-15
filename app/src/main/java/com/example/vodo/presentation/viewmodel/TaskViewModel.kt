package com.example.vodo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.vodo.data.local.database.TaskDatabase
import com.example.vodo.data.local.entity.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(private val database: TaskDatabase):ViewModel() {

    fun getAllTasks(): LiveData<List<TaskEntity>> {
        return database.dao.getTasksOrderByTimeAsc()
    }

    fun getSearchedTasks(searchQuery: String): LiveData<List<TaskEntity>> {
        return database.dao.getSearchedTasks(searchQuery)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteTask(taskEntity: TaskEntity) {
        GlobalScope.launch(Dispatchers.Main) {
            database.dao.deleteTask(taskEntity)
        }
    }



    @OptIn(DelicateCoroutinesApi::class)
    fun addTask(taskEntity: TaskEntity) {
        GlobalScope.launch(Dispatchers.Main) {
            Log.d("VandanAdd",taskEntity.toString())
            database.dao.upsertTask(taskEntity)
        }
    }

}