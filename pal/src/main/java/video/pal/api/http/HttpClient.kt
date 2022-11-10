package video.pal.api.http

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object HttpClient {

    fun getInstance(baseUrl: String, apiToken: String): Retrofit {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(apiToken))
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .create()

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }

    class AuthInterceptor(private val token: String) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
            return chain.proceed(requestBuilder.build())
        }

    }
}