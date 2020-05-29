package com.boronin.test.currencies.features.main

import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.boronin.test.currencies.R
import com.boronin.test.currencies.models.RateModel
import com.boronin.test.currencies.utils.*
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.rates_list_item.view.*
import kotlin.math.roundToInt


class RateVH(
  itemView: View,
  private val rateItemListener: RateItemListener,
  private val inputVal: BehaviorSubject<Float>,
  private val ratesSubject: BehaviorSubject<Map<String, Float>>
) : RecyclerView.ViewHolder(itemView) {
  private val logo = itemView.ivCountry
  private val name = itemView.tvCurrency
  private val desc = itemView.tvCurrencyDesc
  private val value = itemView.etValue
  private var model: RateModel? = null

  init {
    initListeners()
    subscribeToValueUpdate()
    subscribeToRateUpdate()
  }

  fun bind(model: RateModel) {
    this.model = model
    name.text = model.currencyName
    desc.text = model.currencyDesc
    logo.loadImage(getFlagUrl(model), isRound = true)
    updateValue(calc(model))
  }

  // region private

  private fun calc(model: RateModel): Float {
    val rate = model.rate * (inputVal.value ?: DEFAULT_FLOAT)
    return (rate * 100).roundToInt() / 100f
  }

  private fun activateItem(itemView: View) {
    rateItemListener.onSelected(adapterPosition)
    value.requestFocus()
    itemView.context.showKeyboard(value)
  }

  private fun emitNewValue(newValue: Editable?) {
    try {
      model?.let { model ->
        if (model.rate > 0) {
          inputVal.onNext(newValue.toString().toFloat() / model.rate)
        }
      }
    } catch (e: Exception) {
      inputVal.onNext(0f)
    }
  }

  private fun subscribeToValueUpdate() {
    inputVal.subscribe({
      if (adapterPosition > 0) {
        model?.let { updateValue(calc(it)) }
      }
    }, {
      it.printStackTrace()
    }).also { rateItemListener.onSubscribed(it) }
  }

  private fun subscribeToRateUpdate() {
    ratesSubject.subscribe({ ratesMap ->
      if (adapterPosition > 0) {
        model?.let {
          val newRate = ratesMap[it.currencyName] ?: DEFAULT_FLOAT
          model = RateModel(newRate, it.currencyName, it.currencyDesc)
          updateValue(calc(it))
        }
      }
    }, {
      it.printStackTrace()
    }).also { rateItemListener.onSubscribed(it) }
  }

  private fun updateValue(result: Float) {
    val resultStr = when {
      result == DEFAULT_FLOAT -> DEFAULT_STRING
      result > 1e7 -> DEFAULT_STRING
      else -> result.toString()
    }.run { replace(ZERO_END, DEFAULT_STRING) }

    if (resultStr != value.text.toString()) {
      value.setText(resultStr)
    }
  }

  private fun getFlagUrl(model: RateModel): String {
    val name = if (model.currencyName.length > 2) {
      model.currencyName
    } else {
      BASE_CUR
    }.run { substring(0, 2) }
    return itemView.context.getString(R.string.flagUrl, name)
  }

  private fun initListeners() {
    itemView.setOnClickListener { activateItem(itemView) }
    value.addTextWatcher { if (adapterPosition == 0) emitNewValue(it) }
    value.focusChangeListener { hasFocus ->
      if (hasFocus) {
        activateItem(itemView)
        value.setSelection(value?.text?.length ?: 0)
      } else {
        value.clearFocus()
      }
    }
  }

  // endregion
}