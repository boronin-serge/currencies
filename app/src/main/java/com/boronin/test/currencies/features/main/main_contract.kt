package com.boronin.test.currencies.features.main

import com.boronin.test.currencies.models.RateModel


interface MainView {
  fun updateView(list: List<RateModel>)
  fun updateRates(rates: Map<String, Float>)
  fun showError()
  fun setVisibleLoading(visible: Boolean)
}

interface MainAction {
  fun attachViewAction(view: MainView)
  fun moveToTopAction(position: Int)
  fun onPauseAction()
  fun onResumeAction()
}