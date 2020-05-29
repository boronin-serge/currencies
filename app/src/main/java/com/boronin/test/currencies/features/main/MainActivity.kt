package com.boronin.test.currencies.features.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.boronin.test.currencies.App
import com.boronin.test.currencies.R
import com.boronin.test.currencies.models.RateModel
import com.boronin.test.currencies.utils.hideKeyboard
import com.boronin.test.currencies.utils.onTouchListener
import com.boronin.test.currencies.utils.showToast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainView, RateItemListener {
  private lateinit var presenter: MainAction
  private lateinit var inputValSubject: BehaviorSubject<Float>
  private lateinit var rateSubject: BehaviorSubject<Map<String, Float>>

  private val disposables = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    with(application as App) {
      presenter = dependencies.mainPresenter
      rateSubject = dependencies.rateSubject
      inputValSubject = dependencies.inputValSubject
    }
    initList()
    initListeners()
    presenter.attachViewAction(this)
  }

  override fun onDestroy() {
    disposables.dispose()
    super.onDestroy()
  }

  override fun onPause() {
    super.onPause()
    presenter.onPauseAction()
  }

  override fun onResume() {
    super.onResume()
    presenter.onResumeAction()
  }

  // region MainView

  override fun updateRates(rates: Map<String, Float>) = rateSubject.onNext(rates)

  override fun showError() = showToast(R.string.error)

  override fun updateView(list: List<RateModel>) {
    (rvRates.adapter as? RatesAdapter)?.update(list)
  }

  override fun setVisibleLoading(visible: Boolean) {
    progressBar.isVisible = visible
  }

  // endregion


  // region RateItemListener

  override fun onSelected(position: Int) {
    presenter.moveToTopAction(position)
  }

  override fun onSubscribed(disposable: Disposable) {
    disposables.add(disposable)
  }

  // endregion


  // region private

  private fun initListeners() {
    rvRates.onTouchListener { hideKeyboard() }
  }

  private fun initList() {
    rvRates.layoutManager = LinearLayoutManager(this)
    rvRates.adapter = RatesAdapter(inputValSubject, rateSubject, this)
  }

  // endregion
}
