package com.boronin.test.currencies.network

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.GET

interface NamesApi {
  @GET("currencies.json")
  fun getNames(): Single<JsonObject>
}