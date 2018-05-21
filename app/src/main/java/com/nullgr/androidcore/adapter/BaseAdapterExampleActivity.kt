package com.nullgr.androidcore.adapter

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.nullgr.core.adapter.AdapterDelegatesFactory
import com.nullgr.core.adapter.DiffCalculator
import com.nullgr.core.adapter.DynamicAdapter
import com.nullgr.core.adapter.RxDiffCalculator
import com.nullgr.core.rx.RxBus
import com.nullgr.core.rx.schedulers.ComputationToMainSchedulersFacade
import com.nullgr.core.rx.schedulers.SchedulersFacade

/**
 * @author chernyshov.
 */
abstract class BaseAdapterExampleActivity : AppCompatActivity() {

    protected lateinit var schedulersFacade: SchedulersFacade
    protected lateinit var diffCalculator: DiffCalculator
    protected lateinit var bus: RxBus
    protected lateinit var delegatesFactory: AdapterDelegatesFactory
    protected lateinit var adapter: DynamicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    abstract fun provideDelegatesFactory(): AdapterDelegatesFactory

    @CallSuper
    protected open fun init() {
        schedulersFacade = ComputationToMainSchedulersFacade()
        diffCalculator = RxDiffCalculator(schedulersFacade)
        bus = RxBus()
        delegatesFactory = provideDelegatesFactory()
        adapter = DynamicAdapter(delegatesFactory, diffCalculator)
    }
}