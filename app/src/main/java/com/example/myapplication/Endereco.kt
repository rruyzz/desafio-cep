package com.example.myapplication

import java.io.Serializable

data class Endereco(
    val cep: Int,
    val logradouro: String,
    val complemento: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val ibge: Int,
    val gia: Int,
    val ddd: Int,
    val siafi: Int) : Serializable