package com.boronin.test.currencies.utils

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


fun ImageView.loadImage(
  url: String,
  isRound: Boolean = false
) {
  if (url.isNotEmpty()) {
    val creator = Glide.with(this).load(url)
    if (isRound) creator.apply(RequestOptions.circleCropTransform())
    creator.into(this)
  }
}

fun TextView.addTextWatcher(
  beforeTextChangedFun: ((text: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
  onTextChangedFun: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null,
  afterTextChangedFun: ((text: Editable?) -> Unit)? = null
) = object : TextWatcher {
  override fun afterTextChanged(s: Editable?) {
    afterTextChangedFun?.invoke(s)
  }

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    beforeTextChangedFun?.invoke(s, start, count, after)
  }

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    onTextChangedFun?.invoke(s, start, before, count)
  }
}.apply { addTextChangedListener(this) }


fun View.focusChangeListener(focusFun: (Boolean) -> Unit) {
  setOnFocusChangeListener { _, hasFocus -> focusFun.invoke(hasFocus) }
}

@SuppressLint("ClickableViewAccessibility")
fun View.onTouchListener(touchFun: () -> Unit) {
  setOnTouchListener { _, _ ->
    touchFun.invoke()
    false
  }
}