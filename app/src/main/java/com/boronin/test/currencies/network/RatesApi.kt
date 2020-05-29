package com.boronin.test.currencies.network

import com.boronin.test.currencies.network.response.RatesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {
  @GET("latest/")
  fun getRates(@Query("base") base: String): Single<RatesResponse>
}
