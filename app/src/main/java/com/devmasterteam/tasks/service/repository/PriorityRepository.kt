package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context): BaseRepository() {
    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val dataBase = TaskDatabase.getDatabase(context).priorityDao()

    companion object {
        private val cache = mutableMapOf<Int, String>()
        fun getDescription(id: Int): String{
            return cache[id]?: ""
        }
        fun setDescription(id: Int, description: String){
            cache[id] = description
        }
    }

    fun list(listener: APIListener<List<PriorityModel>>){
        val call = remote.list()
        call.enqueue(object: Callback<List<PriorityModel>>{
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                handleResponse(response, listener)
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun getDescription(id: Int): String{
        val cached = PriorityRepository.getDescription(id)
        return if(cached != ""){
            cached
        }else{
            val description = dataBase.getDescription(id)
            PriorityRepository.setDescription(id,description)
            description
        }
    }

    fun save(list: List<PriorityModel>){
        dataBase.clear()
        dataBase.save(list)
    }

    fun getList():List<PriorityModel>{
        return dataBase.list()
    }
}