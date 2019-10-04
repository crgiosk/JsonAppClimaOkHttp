package com.wiedii.jsonclimaapp.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ConexionViewModel(application: Application): AndroidViewModel(application) {
    private var conexion:MutableLiveData<Boolean> = MutableLiveData()
    private val app=application
    fun getConexion():LiveData<Boolean> = conexion
    fun setConexion() { conexion()}

    private fun conexion(){
        val conectManager= app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netWorkInfo=conectManager.activeNetworkInfo
        conexion.value= netWorkInfo != null && netWorkInfo.isConnected

    }
}