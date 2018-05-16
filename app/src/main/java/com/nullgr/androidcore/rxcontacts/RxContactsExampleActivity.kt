package com.nullgr.androidcore.rxcontacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseArray
import android.widget.Toast
import com.nullgr.androidcore.R
import com.nullgr.androidcore.adapter.BaseAdapterExampleActivity
import com.nullgr.androidcore.rxcontacts.items.ContactItem
import com.nullgr.androidcore.rxcontacts.items.HeaderItem
import com.nullgr.corelibrary.adapter.items.ListItem
import com.nullgr.corelibrary.collections.Predicate
import com.nullgr.corelibrary.collections.isNotNullOrEmpty
import com.nullgr.corelibrary.collections.split
import com.nullgr.corelibrary.rx.applySchedulers
import com.nullgr.corelibrary.rx.bindEmpty
import com.nullgr.corelibrary.rx.bindProgress
import com.nullgr.corelibrary.rx.schedulers.IoToMainSchedulersFacade
import com.nullgr.corelibrary.rx.zipWithTimer
import com.nullgr.corelibrary.rxcontacts.RxContactsProvider
import com.nullgr.corelibrary.rxcontacts.domain.UserContact
import com.nullgr.corelibrary.ui.extensions.toggleView
import com.nullgr.corelibrary.widgets.decor.DividerItemDecoration
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_adapter_example.itemsView
import kotlinx.android.synthetic.main.activity_rx_contacts_example_activity.emptyView
import kotlinx.android.synthetic.main.activity_rx_contacts_example_activity.progressView

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class RxContactsExampleActivity : BaseAdapterExampleActivity() {

    private val favoritesPredicates = arrayListOf(
            Predicate<UserContact> { it.isStarred },
            Predicate { !it.isStarred }
    )

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_contacts_example_activity)

        itemsView.layoutManager = LinearLayoutManager(this)
        itemsView.adapter = adapter
        itemsView.addItemDecoration(DividerItemDecoration(this, R.drawable.divider_adapter_item))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {

            disposable = RxContactsProvider.with(this)
                    .fetchAll(UserContact::class.java)
                    .applySchedulers(IoToMainSchedulersFacade())
                    .zipWithTimer(1000)
                    .bindProgress(Consumer { runOnUiThread { progressView.toggleView(it) } })
                    .bindEmpty(Consumer { runOnUiThread { emptyView.toggleView(it) } })
                    .map {
                        mapToListItems(it.split(favoritesPredicates))
                    }.subscribe({
                        adapter.updateData(it)
                    }, {
                        Toast.makeText(this, "Error Occurred: $it", Toast.LENGTH_SHORT).show()
                    })
        }
    }

    private fun mapToListItems(result: SparseArray<List<UserContact>>): List<ListItem> {
        return arrayListOf<ListItem>().apply {
            val favorites = result[0]
            if (favorites.isNotNullOrEmpty()) {
                add(HeaderItem(getString(R.string.title_fav_contacts)))
                addAll(favorites.toContactItems())
            }
            val otherContacts = result[1]
            if (otherContacts.isNotNullOrEmpty()) {
                add(HeaderItem(getString(R.string.title_other_contacts)))
                addAll(otherContacts.toContactItems())
            }
        }
    }

    override fun provideDelegatesFactory() = ContactsDelegateFactory()

    override fun onDestroy() {
        super.onDestroy()
        if (this::disposable.isInitialized)
            disposable.dispose()
    }

    private fun List<UserContact>.toContactItems(): List<ContactItem> {
        return asSequence().map {
            ContactItem(it.id,
                    it.displayName,
                    it.isStarred,
                    it.photo,
                    it.phones?.map { it.phoneNumber!! },
                    it.emails?.map { it.email!! })
        }.sortedBy {
            it.displayName
        }.toList()
    }
}