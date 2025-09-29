package task.app.menuitem.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import task.app.menuitem.utils.ConstantUtils
import java.util.concurrent.TimeUnit

/**
 * Singleton object responsible for providing a instance of Retrofit
 * and exposing the API service used for making network calls.
 */
object RetrofitClient {

    // Logging interceptor to log HTTP request and response data (for debugging purposes)
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE // Logs request/response level (BODY for debugging)
    }

    private fun getRetrofit(): Retrofit {
        // Configure OkHttpClient with custom timeout and logging
        val httpClient: OkHttpClient.Builder  = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
        val retrofit = Retrofit.Builder()
            .baseUrl(ConstantUtils.BASE_URL) // Base URL from ConstantsUtils
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON parsing
            .client(httpClient.build()
            ).
            build()
        return retrofit
    }

    //Initialize API service instance that can be used throughout the app to make API calls.
    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}