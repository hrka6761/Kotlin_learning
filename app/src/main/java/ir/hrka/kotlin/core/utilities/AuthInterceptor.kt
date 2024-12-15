package ir.hrka.kotlin.core.utilities

import ir.hrka.kotlin.core.Constants.TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        requestBuilder.addHeader("Authorization", TOKEN)

        return chain.proceed(requestBuilder.build())
    }
}