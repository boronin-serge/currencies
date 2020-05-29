package com.boronin.test.currencies.features.main

import androidx.recyclerview.widget.DiffUtil
import com.boronin.test.currencies.models.RateModel

class RateDiffUtilCallback(
  private val oldList: List<RateModel>,
  private val newList: List<RateModel>
) : DiffUtil.Callback() {

  override fun getOldListSize() = oldList.size

  override fun getNewListSize() = newList.size

  override fun areContentsTheSame(oldIndex: Int, newIndex: Int): Boolean {
    val oldItem = oldList[oldIndex]
    val newItem = newList[newIndex]
    return oldItem == newItem
  }

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
    areContentsTheSame(oldItemPosition, newItemPosition)
}

