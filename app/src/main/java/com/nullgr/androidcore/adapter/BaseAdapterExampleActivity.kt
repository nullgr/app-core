package com.nullgr.androidcore.adapter

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nullgr.core.adapter.AdapterDelegatesFactory
import com.nullgr.core.adapter.DynamicAdapter
import com.nullgr.core.adapter.bindTo
import com.nullgr.core.adapter.items.ListItem
import com.nullgr.core.rx.RxBus
import com.nullgr.core.rx.schedulers.ComputationToMainSchedulersFacade
import com.nullgr.core.rx.schedulers.SchedulersFacade
import io.reactivex.disposables.CompositeDisposable

/**
 * @author chernyshov.
 */
abstract class BaseAdapterExampleActivity : AppCompatActivity() {

    protected lateinit var bus: RxBus
    protected lateinit var delegatesFactory: AdapterDelegatesFactory
    protected lateinit var schedulersFacade: SchedulersFacade
    protected lateinit var adapter: DynamicAdapter
    protected val compositeUnBind = CompositeDisposable()
    protected val itemsRelay = BehaviorRelay.create<List<ListItem>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        itemsRelay.bindTo(adapter, compositeUnBind)
    }

    override fun onStop() {
        compositeUnBind.clear()
        super.onStop()

    }

    abstract fun provideDelegatesFactory(): AdapterDelegatesFactory

    @CallSuper
    protected open fun init() {
        bus = RxBus()
        schedulersFacade = ComputationToMainSchedulersFacade()
        delegatesFactory = provideDelegatesFactory()
        adapter = DynamicAdapter(delegatesFactory)
    }
}