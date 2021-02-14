package com.example.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
@Parcelize
data class Endereco(
    var cep: String,
    var logradouro: String,
    var complemento: String,
    var bairro: String
//    val localidade: String,
//    val uf: String,
//    val ibge: Int,
//    val gia: Int,
//    val ddd: Int,
//    val siafi: Int
) : Parcelable