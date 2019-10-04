package com.wiedii.jsonclimaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.wiedii.jsonclimaapp.network.ConexionViewModel
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import com.google.gson.Gson
import com.wiedii.jsonclimaapp.Helpers.Ciudad
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var conexionViewModel: ConexionViewModel

    private val miApikey="35e3b8a6ae50f62b6c6525a620c99529"
    private var conexion: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        conexionViewModel = ViewModelProviders.of(this).get(
            ConexionViewModel(this.application)::class.java
        )

        conexionViewModel.getConexion().observe(this, Observer {
            conexion = it
        })

        //peticionOkHtttp("https://jsonplaceholder.typicode.com/posts/")
        peticionOkHtttp("https://api.openweathermap.org/data/2.5/weather?id=3685533&lang=es&units=metric&appid=35e3b8a6ae50f62b6c6525a620c99529")
                          //  http@ //api.openweathermap.org/data/2.5/weather?q=Bogota&APPID=35e3b8a6ae50f62b6c6525a620c99529

        /*
        units para los grados units=metric
        lang lenguaje  lang=es

         */
    }

    private fun estateConexion(): Boolean {
        conexionViewModel.setConexion()
        return conexion != null && conexion != false
    }

    private fun getFile() {
        if (estateConexion()) {
            /*mi apikey a2d01af237aaacc94a9621eb95a92071
            id de mi ciudad city/   3685533
             para pedir por el nombre de la ciudad: api.openweathermap.org/data/2.5/weather?q=Colombia
             para pedir por el Id:    http://api.openweathermap.org/data/2.5/weather?id=2172797&appid=$miApikey
             para pedir por longitud/latitud: api.openweathermap.org/data/2.5/weather?lat=35&lon=139
            ref https://openweathermap.org/current
            */
        } else {
            showError()
        }
    }

    private fun showError() {
        Snackbar.make(findViewById(android.R.id.content), "Verifique su internte", Snackbar.LENGTH_LONG).show()
    }

    private fun peticionOkHtttp(url: String) {
        val cliente = OkHttpClient()
        val peticion = okhttp3.Request.Builder().url(url).build()
        cliente.newCall(peticion).enqueue(
            object : okhttp3.Callback {

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("fallo", " on fiule ${e.message}    ${call.toString()}")
                }
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val result = response.body()!!.string()
                        this@MainActivity.runOnUiThread {
                            try {
                                Log.d("resultOkhhtp3", result)
                                val json= Gson()

                                val ciudad=json.fromJson(result,Ciudad::class.java)

                                Log.d("ciudad","${ciudad.name}  ${ciudad.weather!!.get(0).description}")
                                //nombre de la ciudad
                                textViewCity.text=ciudad.name
                                //Grados
                                textViewGrados.text="${ciudad.main!!.temp}"
                                //nubes
                                textViewDescription.text=ciudad.weather!!.get(0).description


                                //val json= JSONObject(response.toString())
                                //val testArray=json.getJSONArray("")

                                    //Log.d("jsonTest",testArray.getJSONObject(0).getString("body"))

                            } catch (e: Exception) {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Error inesperado: ${e.message}",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }

                        }
                    }else{
                        Log.e("response.failure","Aglo salio mal $call   response: ${response.body()!!.string()}")
                    }
                }
            }
        )
    }
}
