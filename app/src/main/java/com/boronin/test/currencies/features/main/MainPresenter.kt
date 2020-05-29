package com.boronin.test.currencies.features.main

import com.boronin.test.currencies.models.RateModel
import com.boronin.test.currencies.network.response.RatesResponse
import com.boronin.test.currencies.network.source.NetworkSource
import com.boronin.test.currencies.utils.BASE_CUR
import com.boronin.test.currencies.utils.DEFAULT_STRING
import com.boronin.test.currencies.utils.progress
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainPresenter(private val networkSource: NetworkSource) : MainAction {
  private var view: MainView? = null
  private var rates = mutableListOf<RateModel>()
  private var names = mapOf<String, String>()
  private var disposables = CompositeDisposable()
  private var rateUpdatesDisposable: Disposable? = null

  override fun attachViewAction(view: MainView) {
    this.view = view

    when {
      names.isEmpty() -> loadNames()
      rates.isEmpty() -> updateRates()
      else -> {
        subscribeToRateUpdates()
        view.updateView(rates)
        view.setVisibleLoading(false)
      }
    }
  }

  // region MainAction

  override fun onPauseAction() {
    disposables.dispose()
    rateUpdatesDisposable?.dispose()
  }

  override fun onResumeAction() {
    subscribeToRateUpdates()
  }

  override fun moveToTopAction(position: Int) {
    val item = rates[position]
    rates.removeAt(position)
    rates.add(0, item)
    view?.updateView(rates)
  }

  // endregion


  // region private

  private fun getDesc(name: String) = names[name] ?: name

  private fun loadNames() {
    disposables add networkSource.loadNames()
      .doOnSuccess { updateRates() }
      .subscribe({
        names = mapNamesResponse(it)
      }, {
        view?.showError()
      })
  }

  private fun updateRates() {
    disposables add networkSource.loadRates(BASE_CUR)
      .progress { view?.setVisibleLoading(it) }
      .doOnSuccess { subscribeToRateUpdates() }
      .subscribe({ response ->
        rates = mapRatesResponse(response)
        view?.updateView(rates)
      }, {
        view?.showError()
      })
  }


  private fun subscribeToRateUpdates() {
    if (rateUpdatesDisposable?.isDisposed != false) {
      rateUpdatesDisposable = networkSource.getRateUpdates(BASE_CUR)
        .subscribe({ response ->
          val ratesJson = response.rates ?: JsonObject()
          val map = hashMapOf(BASE_CUR to 1f)
          ratesJson.keySet().toList().map { currency ->
            map[currency] = ratesJson.get(currency)?.asFloat ?: 1f
          }
          view?.updateRates(map)
        }, {
          view?.showError()
        })
    }
  }

  private fun mapNamesResponse(namesJson: JsonObject): Map<String, String> {
    return namesJson.keySet().toList().map { currency ->
      val fullName = namesJson.get(currency)?.asString ?: DEFAULT_STRING
      currency to fullName
    }.toMap()
  }

  private fun mapRatesResponse(response: RatesResponse): MutableList<RateModel> {
    val ratesJson = response.rates ?: JsonObject()
    val items = mutableListOf(
      RateModel(1f, BASE_CUR, getDesc(BASE_CUR))
    )
    ratesJson.keySet().map { currency ->
      val rate = ratesJson.get(currency)?.asFloat ?: 1f
      items.add(RateModel(rate, currency, getDesc(currency)))
    }
    return items.sortedBy { it.currencyName }.toMutableList()
  }

  // endregion

}

infix fun CompositeDisposable.add(disposable: Disposable) {
  add(disposable)
}