package com.boronin.test.currencies.network.response

import com.google.gson.JsonObject

data class RatesResponse(
  val baseCurrency: String?,
  val rates: JsonObject?
)