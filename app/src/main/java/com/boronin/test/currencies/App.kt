package com.boronin.test.currencies

import android.app.Application
import com.boronin.test.currencies.deps.Dependencies

class App : Application() {
  val dependencies: Dependencies by lazy { Dependencies() }
}