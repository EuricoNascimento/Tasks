package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.TaskRepository
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)
    private val _listPriority = MutableLiveData<List<PriorityModel>>()
    val listPriority: LiveData<List<PriorityModel>> = _listPriority
    private val _saveTask = MutableLiveData<ValidationModel>()
    var saveTask: LiveData<ValidationModel> = _saveTask

    fun loadPriority(){
        _listPriority.value = priorityRepository.getList()
    }

    fun save(task: TaskModel) {
        val personKey = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        RetrofitClient.addHeaders(token, personKey)
        taskRepository.create(task, object : APIListener<Boolean> {
            override fun onSucess(result: Boolean) {
                _saveTask.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _saveTask.value = ValidationModel(message)
            }

        })
    }
}