package com.boronin.test.currencies.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context.hideKeyboard(view: View?) {
  try {
    if (view != null && view.windowToken != null) {
      val inputMethodService = getSystemService(Context.INPUT_METHOD_SERVICE)
      val inputMethod = inputMethodService as? InputMethodManager
      inputMethod?.hideSoftInputFromWindow(view.windowToken, 0)
    }
  } catch (t: Throwable) {
    t.printStackTrace()
  }
}

fun Context.showKeyboard(view: View?) {
  try {
    if (view != null) {
      val inputMethodService = getSystemService(Context.INPUT_METHOD_SERVICE)
      val inputMethod = inputMethodService as? InputMethodManager
      inputMethod?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
  } catch (t: Throwable) {
    t.printStackTrace()
  }
}

fun Context.showToast(resId: Int) {
  Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}