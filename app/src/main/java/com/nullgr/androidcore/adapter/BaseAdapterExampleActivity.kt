package com.nullgr.androidcore.adapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nullgr.corelibrary.adapter.AdapterDelegatesFactory
import com.nullgr.corelibrary.adapter.DiffCalculator
import com.nullgr.corelibrary.adapter.DynamicAdapter
import com.nullgr.corelibrary.adapter.RxDiffCalculator
import com.nullgr.corelibrary.rx.RxBus
import com.nullgr.corelibrary.rx.schedulers.ComputationToMainSchedulersFacade
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade

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

    private fun init() {
        schedulersFacade = ComputationToMainSchedulersFacade()
        diffCalculator = RxDiffCalculator(schedulersFacade)
        bus = RxBus()
        provideDelegatesFactory()
        adapter = DynamicAdapter(delegatesFactory, diffCalculator)
    }

    protected open fun provideDelegatesFactory() {
        delegatesFactory = ExampleDelegatesFactory(bus)
    }
}