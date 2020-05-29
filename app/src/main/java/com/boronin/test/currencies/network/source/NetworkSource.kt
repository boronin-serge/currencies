package com.boronin.test.currencies.network.source

import com.boronin.test.currencies.network.NamesApi
import com.boronin.test.currencies.network.RatesApi
import com.boronin.test.currencies.utils.TICK_FOR_UPDATE
import com.boronin.test.currencies.utils.schedulers
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class NetworkSource(private val ratesApi: RatesApi, private val namesApi: NamesApi) {
  fun loadRates(baseCurrency: String) = ratesApi.getRates(baseCurrency).schedulers()

  fun getRateUpdates(baseCurrency: String) = Observable.interval(TICK_FOR_UPDATE, TimeUnit.SECONDS)
    .flatMap { loadRates(baseCurrency).toObservable() }
    .schedulers()

  fun loadNames() = namesApi.getNames().schedulers()
}