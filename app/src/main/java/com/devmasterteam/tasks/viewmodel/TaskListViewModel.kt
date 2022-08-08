package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository
import com.devmasterteam.tasks.service.repository.local.TaskDatabase

class TaskListViewModel (application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val _listTask = MutableLiveData<List<TaskModel>>()
    val listTask: LiveData<List<TaskModel>> = _listTask
    private val _loadList = MutableLiveData<ValidationModel>()
    val loadList: LiveData<ValidationModel> = _loadList
    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    fun list(){
        taskRepository.listAll(object: APIListener<List<TaskModel>>{
            override fun onSucess(result: List<TaskModel>) {
                result.forEach{
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }

                _listTask.value = result
            }

            override fun onFailure(message: String) {
                _loadList.value = ValidationModel(message)
            }
        })
    }

    fun delete(id: Int){
        taskRepository.delete(id, object: APIListener<Boolean>{
            override fun onSucess(result: Boolean) {
                list()
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }

        })
    }
}