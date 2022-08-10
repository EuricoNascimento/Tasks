package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository( context: Context): BaseRepository(context) {
    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun create(task: TaskModel, listener: APIListener<Boolean>){
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener)
    }

    fun listAll(listener: APIListener<List<TaskModel>>){
        val call = remote.list()
        executeCall(call, listener)
    }

    fun listNextDays(listener: APIListener<List<TaskModel>>){
        val call = remote.listNextDays()
        executeCall(call, listener)
    }

    fun listOverdue(listener: APIListener<List<TaskModel>>){
        val call = remote.listOverdue()
        executeCall(call, listener)
    }

    fun delete(id: Int, listener: APIListener<Boolean>){
        val call = remote.delete(id)
        executeCall(call, listener)
    }

}