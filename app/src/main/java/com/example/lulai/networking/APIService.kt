package com.example.lulai.networking

import android.util.Log
import com.example.lulai.constants.Constants // Correct the import here
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class APIService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    fun signUp(
        username: String,
        email: String,
        password: String,
        latitude: Double?,
        longitude: Double?,
        completion: (Boolean) -> Unit
    ) {
        val url = "${Constants.API_BASE_URL}/api/auth/signup" // Use Constants here
        val json = JSONObject().apply {
            put("UserName", username)
            put("UserEmail", email)
            put("UserPassword", password)
            if (latitude != null && longitude != null) {
                put("UserLocation", JSONObject().apply {
                    put("latitude", latitude)
                    put("longitude", longitude)
                })
            }
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder().url(url).post(body).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e("APIService", "SignUp failed: ${e.message}")
                completion(false)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    Log.d("APIService", "SignUp successful!")
                    completion(true)
                } else {
                    Log.e("APIService", "SignUp failed: ${response.message}")
                    completion(false)
                }
            }
        })
    }
}
