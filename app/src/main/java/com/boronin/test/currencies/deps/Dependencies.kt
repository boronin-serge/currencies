package com.boronin.test.currencies.deps

import com.boronin.test.currencies.features.main.MainAction
import com.boronin.test.currencies.features.main.MainPresenter
import com.boronin.test.currencies.network.ApiConfig
import com.boronin.test.currencies.network.NamesApi
import com.boronin.test.currencies.network.RatesApi
import com.boronin.test.currencies.network.RetrofitWrapper
import com.boronin.test.currencies.network.source.NetworkSource
import io.reactivex.subjects.BehaviorSubject

class Dependencies {

  val mainPresenter: MainAction by lazy {
    MainPresenter(networkSource)
  }

  val inputValSubject: BehaviorSubject<Float> by lazy {
    BehaviorSubject.create<Float>()
  }

  val rateSubject: BehaviorSubject<Map<String, Float>> by lazy {
    BehaviorSubject.create<Map<String, Float>>()
  }

  // region private

  private val networkRates: RatesApi by lazy {
    RetrofitWrapper(ApiConfig.BASE_URL).create(RatesApi::class)
  }

  private val networkNames: NamesApi by lazy {
    RetrofitWrapper(ApiConfig.NAME_API_URL).create(NamesApi::class)
  }

  private val networkSource: NetworkSource by lazy {
    NetworkSource(networkRates, networkNames)
  }

  // endregion
}