package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) {
    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val dataBase = TaskDatabase.getDatabase(context).priorityDao()

    fun list(listener: APIListener<List<PriorityModel>>){
        val call = remote.list()
        call.enqueue(object: Callback<List<PriorityModel>>{
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                response.body()?.let { listener.onSucess(it) }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun save(list: List<PriorityModel>){
        dataBase.clear()
        dataBase.save(list)
    }

    fun getList():List<PriorityModel>{
        return dataBase.list()
    }
}