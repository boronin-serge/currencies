package com.boronin.test.currencies.utils

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// region single

fun <T> Single<T>.schedulers() = schedulers(Schedulers.io(), AndroidSchedulers.mainThread())

fun <T> Single<T>.schedulers(
  subscribeOnScheduler: Scheduler,
  observeOnScheduler: Scheduler
): Single<T> = compose {
  it.subscribeOn(subscribeOnScheduler)
    .observeOn(observeOnScheduler)
}

fun <T> Single<T>.progress(progressFun: (Boolean) -> Unit): Single<T> = compose {
  it.doOnSubscribe { progressFun.invoke(true) }
    .doAfterTerminate { progressFun.invoke(false) }
}

// endregion


// region observable

fun <T> Observable<T>.schedulers() = schedulers(Schedulers.io(), AndroidSchedulers.mainThread())

fun <T> Observable<T>.schedulers(
  subscribeOnScheduler: Scheduler,
  observeOnScheduler: Scheduler
): Observable<T> = compose {
  it.subscribeOn(subscribeOnScheduler)
    .observeOn(observeOnScheduler)
}

fun <T> Observable<T>.progress(progressFun: (Boolean) -> Unit): Observable<T> = compose {
  it.doOnSubscribe { progressFun.invoke(true) }
    .doAfterTerminate { progressFun.invoke(false) }
}

// endregion