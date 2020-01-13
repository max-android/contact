package com.my_project.contactlist.presentation.contacts

import android.Manifest
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my_project.contactlist.R
import com.my_project.contactlist.data.entities.Contact
import com.my_project.contactlist.presentation.adapters.BaseType
import com.my_project.contactlist.presentation.adapters.ContactAdapter
import com.my_project.contactlist.presentation.adapters.ContactMapper
import com.my_project.contactlist.presentation.adapters.ContactTitleType
import com.my_project.contactlist.presentation.base.BaseFragment
import com.my_project.contactlist.utilities.DialogUtils
import com.my_project.contactlist.utilities.gone
import com.my_project.contactlist.utilities.mainActivity
import com.my_project.contactlist.utilities.visible
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_contacts.*
import timber.log.Timber

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class ContactsFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): Fragment = ContactsFragment()
    }

    private lateinit var viewModel: ContactsViewModel
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var rxPermissions: RxPermissions
    private val subscriptions = CompositeDisposable()
    override fun getLayoutRes(): Int = R.layout.fragment_contacts

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    private fun init() {
        rxPermissions = RxPermissions(this)
        updateActionBar()
        setListener()
        initAdapter()
        observeData()
        loadContacts()
    }

    private fun updateActionBar() {
        val actionBar = mainActivity?.supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            title = getString(R.string.contacts)
        }
    }

    private fun setListener() {
        addFab.setOnClickListener { viewModel.addContact() }
        contactButton.setOnClickListener { loadContactFromPhone() }
    }

    private fun initAdapter() {
        contactAdapter = ContactAdapter()
        contactsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            adapter = contactAdapter
        }
    }

    private fun loadContacts() = viewModel.contacts()

    private fun loadContactFromPhone(){
        rxPermissions
            .request(Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS)
            .subscribe({
                if (it) viewModel.loadFromPhone(context!!.contentResolver)
            }, {
                Timber.e(it)
            }).addTo(subscriptions)

    }

    private fun observeData() = viewModel.liveData.observe(this, Observer{ processViewState(it) })

    private fun processViewState(viewState: ContactViewState?) {
        viewState?.let {
            when (it) {
                is SuccessContacts -> showContacts(it.contacts)
                is Error -> showError(it.error)
            }
        }
    }

    private fun showContacts(contacts: List<Contact>) {
        contactAdapter.swapItems(ContactMapper().orderedList(contacts))
        contactAdapter.onItemClick { onItemCityClick(it) }
        emptyImageView.gone()
        contactsRecyclerView.visible()
    }

    private fun showError(error: Throwable) {
        Timber.tag("--SAMPLE-e").i(error.toString())
        Timber.e(error)
        emptyImageView.visible()
    }

    private fun onItemCityClick(type: BaseType) {
        DialogUtils(context!!).showMessage(
            { viewModel.deleteContact(type)},
            { viewModel.editContact(type)})
    }

}


