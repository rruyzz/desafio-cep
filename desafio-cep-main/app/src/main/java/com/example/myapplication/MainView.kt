package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainView(val service: Service) : ViewModel() {

    val adress = MutableLiveData<Endereco>()

    fun getEndereco(cep: String): MutableLiveData<Endereco> {
        viewModelScope.launch{
            adress.value = service.getEnderecoService(cep)
        }
        return adress
    }
}