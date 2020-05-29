package com.boronin.test.currencies.features.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.boronin.test.currencies.R
import com.boronin.test.currencies.models.RateModel
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

class RatesAdapter(
  private val inputValSubject: BehaviorSubject<Float>,
  private val rateSubject: BehaviorSubject<Map<String, Float>>,
  private val rateItemListener: RateItemListener
) : RecyclerView.Adapter<RateVH>() {
  private var items = mutableListOf<RateModel>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RateVH(
    LayoutInflater.from(parent.context).inflate(R.layout.rates_list_item, parent, false),
    rateItemListener,
    inputValSubject,
    rateSubject
  )

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: RateVH, position: Int) = holder.bind(items[position])

  fun update(items: List<RateModel>) {
    calcDiff(this.items, items).also {
      this.items.clear()
      this.items.addAll(items)
      it.dispatchUpdatesTo(this)
    }
  }

  // region private

  private fun calcDiff(oldList: List<RateModel>, newList: List<RateModel>): DiffUtil.DiffResult {
    val callback = RateDiffUtilCallback(oldList, newList)
    return DiffUtil.calculateDiff(callback)
  }

  // endregion

}

interface RateItemListener {
  fun onSelected(position: Int)
  fun onSubscribed(disposable: Disposable)
}