package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)
    private val _login = MutableLiveData<ValidationModel>()
    var login: LiveData<ValidationModel> = _login
    private val _loggedUser = MutableLiveData<Boolean>()
    var loggedUser: LiveData<Boolean> = _loggedUser

    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object: APIListener<PersonModel>{
            override fun onSucess(result: PersonModel) {
                //Salva no shared preferences para continuar logado
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                //Salva no header para obter ba lista de tasks
                RetrofitClient.addHeaders(result.token, result.personKey)
                _login.value = ValidationModel()
            }

            override fun onFailure(message: String) { _login.value = ValidationModel(message)
            }

        })
    }

    //Verifica se usuario está logado
    fun verifyLoggedUser(){
        val personKey = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        RetrofitClient.addHeaders(token, personKey)

        val logged = (personKey != "" && token != "")
        _loggedUser.value = logged
        if(!logged) {
            downloadPriority()
        }
    }

    //Faz o download das prioridades
    private fun downloadPriority(){
        priorityRepository.list(object: APIListener<List<PriorityModel>>{
            override fun onSucess(result: List<PriorityModel>) {
                priorityRepository.save(result)
            }

            override fun onFailure(message: String) {

            }

        })
    }
}