package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

open class ViewModel(private val respository: Respository) : ViewModel() {
//    var response : MutableLiveData<Response<Endereco>> = MutableLiveData()
//
//    fun getEndereco(cep: Int){
//        viewModelScope.launch {
//            val response = respository.getEndereco(cep)
//            response.
//        }
//    }
}