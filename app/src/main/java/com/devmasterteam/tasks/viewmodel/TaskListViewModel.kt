package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel (application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val _listTask = MutableLiveData<List<TaskModel>>()
    val listTask: LiveData<List<TaskModel>> = _listTask
    private val _loadList = MutableLiveData<ValidationModel>()
    val loadList: LiveData<ValidationModel> = _loadList

    fun list(){
        taskRepository.listAll(object: APIListener<List<TaskModel>>{
            override fun onSucess(result: List<TaskModel>) {
                _listTask.value = result
            }

            override fun onFailure(message: String) {
                _loadList.value = ValidationModel(message)
            }
        })
    }
}