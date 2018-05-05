package com.nullgr.androidcore.adapter

import android.support.v7.app.AppCompatActivity
import com.nullgr.corelibrary.adapter.AdapterDelegatesFactory
import com.nullgr.corelibrary.adapter.DiffCalculator
import com.nullgr.corelibrary.adapter.DynamicAdapter
import com.nullgr.corelibrary.adapter.RxDiffCalculator
import com.nullgr.corelibrary.rx.schedulers.ComputationToMainSchedulersFacade
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade

/**
 * @author chernyshov.
 */
abstract class BaseAdapterExampleActivity : AppCompatActivity() {

    protected val schedulersFacade: SchedulersFacade
    protected val diffCalculator: DiffCalculator
    protected val delegatesFactory: AdapterDelegatesFactory
    protected val adapter: DynamicAdapter

    init {
        schedulersFacade = ComputationToMainSchedulersFacade()
        diffCalculator = RxDiffCalculator(schedulersFacade)
        delegatesFactory = ExampleDelegatesFactory()
        adapter = DynamicAdapter(delegatesFactory, diffCalculator)
    }
}