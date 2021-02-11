package com.example.myapplication

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET("{cep}/json")
    suspend fun getEndereco( @Path("cep") cep: String) : Call<Endereco>
}

//val retrofit = Retrofit.Builder()
//    .baseUrl("https://viacep.com.br/ws")
//    .addConverterFactory(GsonConverterFactory.create())
//    .build()
//
//val service: Service = retrofit.create(Service::class.java)