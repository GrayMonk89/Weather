package com.gb.weather.view.details

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.gb.weather.BuildConfig
import com.gb.weather.repository.OnServerResponse
import com.gb.weather.repository.OnServerResponseListener
import com.gb.weather.repository.dto.WeatherDTO
import com.gb.weather.utils.*
import com.gb.weather.viewmodel.ResponseState
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL

class DetailsService(
    serviceName: String = "Service"
) : IntentService(serviceName) {

    //private val onErrorListener = OnServerResponseListener()

    override fun onHandleIntent(intent: Intent?) {
        Log.d(LOG_KEY, "Сервис DetailsService заработал")
        //TODO вызвать Snackbar

        intent?.let {
            val lat = it.getDoubleExtra(LAT_KEY,0.0)
            val lon = it.getDoubleExtra(LON_KEY,0.0)
            Log.d(LOG_KEY, "Получили широту и долготу: $lat $lon")

            //val urlText = $YANDEX_DOMAIN_PART$YANDEX_ENDPOINT$LAT_KEY$=lat$LON_KEY=$lon
            val urlText = "$YANDEX_DOMAIN_HARD_MODE_PART$YANDEX_ENDPOINT$LAT_KEY=$lat&$LON_KEY=$lon"
            val uri = URL(urlText)
            //val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply {
            val urlConnection: HttpURLConnection =
                (uri.openConnection() as HttpURLConnection).apply {
                    connectTimeout = CONNECT_TIMEOUT
                    readTimeout = READ_TIMEOUT
                    addRequestProperty(
                        YANDEX_API_KEY,
                        BuildConfig.WEATHER_API_KEY
                    )
                }

            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage

                Log.d(LOG_KEY, "DetailsService responseCode: $responseCode   responseMessage: $responseMessage")

                if (responseCode in SERVER_SIDE) {

                    //onErrorListener.onError(ResponseState.ErrorOnServerSide("responseCode: $responseCode   responseMessage: $responseMessage"))

                } else if (responseCode in CLIENT_SIDE) {

                    //onErrorListener.onError(ResponseState.ErrorOnClientSide("responseCode: $responseCode   responseMessage: $responseMessage"))

                } else if (responseCode in RESPONSEOK) {
                    val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO: WeatherDTO = Gson().fromJson(
                        buffer,
                        WeatherDTO::class.java
                    )

                    val message = Intent(BROADCAST_RECEIVER_CHANNEL_WEATHER_KEY)
                    message.putExtra(SERVICE_BROADCAST_WEATHER_KEY, weatherDTO)
                    sendBroadcast(message)
                    //LocalBroadcastManager.getInstance(this).sendBroadcast(message)

                    /*Handler(Looper.getMainLooper()).post {
                        onServerResponseListener.onResponse(weatherDTO)
                    }*/
                }
            } catch (e: JsonSyntaxException) {
                //onErrorListener.onError(ResponseState.ErrorInJSONConversion(e.toString()))
            } catch (e: RuntimeException){

            }
            finally {
                urlConnection.disconnect()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_KEY, "Сервис DetailsService завершил работу")
    }
}