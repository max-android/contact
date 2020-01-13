package com.my_project.contactlist.presentation.change

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.my_project.contactlist.R
import com.my_project.contactlist.data.entities.Contact
import com.my_project.contactlist.presentation.base.BaseFragment
import com.my_project.contactlist.utilities.gone
import com.my_project.contactlist.utilities.mainActivity
import com.my_project.contactlist.utilities.visible
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_change.*
import timber.log.Timber

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class ChangeFragment : BaseFragment() {

    companion object {
        const val PROFILE_IMAGE_REQUEST_CODE = 103
        const val TYPE_KEY = "type_key"
        const val CHANGE_KEY = "change_key"
        fun newInstance(edit: Boolean, contact: Contact?): ChangeFragment {
            val fragment = ChangeFragment()
            val args = Bundle()
            args.putBoolean(TYPE_KEY, edit)
            if (edit) {
                contact?.let { args.putSerializable(CHANGE_KEY, it) }
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: ChangeViewModel
    private var type: Boolean? = null
    private var contact: Contact? = null
    private var actionBar: ActionBar? = null
    private var typeContact: String = ""
    private var update: Boolean = false
    private var urlProfile: String? = null
    private val subscriptions = CompositeDisposable()
    private lateinit var rxPermissions: RxPermissions
    override fun getLayoutRes(): Int = R.layout.fragment_change

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangeViewModel::class.java)
        init(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PROFILE_IMAGE_REQUEST_CODE && data != null) {
            data.data?.let {
                showProfile(it)
                urlProfile = it.toString()
            }
        }
    }

    override fun onDestroyView() {
        saveViewsState()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    private fun init(savedInstanceState: Bundle?) {
        initType()
        updateActionBar()
        if (savedInstanceState == null) {
            initData()
        } else {
            dataFromViewModel()
        }
        setListeners()
    }

    private fun getContact() = arguments?.getSerializable(CHANGE_KEY) as? Contact

    private fun initType() {
        type = arguments?.getBoolean(TYPE_KEY)
    }

    private fun updateActionBar() {
        actionBar = mainActivity?.supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initData() {
        type?.let {
            if (it) {
                update = true
                actionBar?.title = getString(R.string.update_contact)
                contact = getContact()
                typeContact = contact!!.type
                urlProfile = contact?.url
                contact?.let { contact -> showContact(contact) }
            } else {
                update = false
                actionBar?.title = getString(R.string.add_contact)
                typeContact = getString(R.string.friends)
            }
            updateUi(typeContact)
        }
    }

    private fun dataFromViewModel() {
        typeContact = viewModel.vmTypeContact!!
        updateUi(typeContact)
        urlProfile = viewModel.vmUrlProfile
        urlProfile?.let { profileImage.setImageURI(Uri.parse(it)) }
        viewModel.vmSurname?.let { surnameEditText.setText(it) }
        viewModel.vmName?.let { nameEditText.setText(it) }
        viewModel.vmPatronymic?.let { patronymicEditText.setText(it) }
        viewModel.vmPhone?.let { phoneEditText.setText(it) }
        type?.let {
            if (it) {
                update = true
                actionBar?.title = getString(R.string.update_contact)
                viewModel.vmBirthdate?.let { birthdate -> birthdateEditText.setText(birthdate) }
            } else {
                update = false
                actionBar?.title = getString(R.string.add_contact)
                viewModel.vmPosition?.let { position -> positionEditText.setText(position) }
                viewModel.vm_office_phone?.let { office_phone ->
                    officePhoneEditText.setText(
                        office_phone
                    )
                }
            }
        }
    }

    private fun showContact(contact: Contact) {
        contact.url?.let {
            val uri = Uri.parse(it)
            profileImage.setImageURI(uri)
        }
        if(contact.surname == "Фамилия"){
            viewModel.phone_contact = "Фамилия"
            viewModel.phone_contact_id = contact.id.toString()
        }
        surnameEditText.setText(contact.surname)
        nameEditText.setText(contact.name)
        contact.patronymic?.let { patronymicEditText.setText(it) }
        phoneEditText.setText(contact.phone)

        if (contact.type == getString(R.string.friends)) {
            friendsFieldsVisible()
            contact.birthdate?.let { birthdateEditText.setText(it) }
        }

        if (contact.type == getString(R.string.colleagues)) {
            colleaguesFieldsVisible()
            contact.position?.let { positionEditText.setText(it) }
            contact.office_phone?.let { officePhoneEditText.setText(it) }
        }
    }

    private fun setListeners() {
        observeSaveButtonState()
        observeSaveButtonClicks()
        observeSimpleDraweeView()
        setSpinnerListener()
    }

    private fun observeSaveButtonState() {
        Observables.combineLatest(
            surnameEditText.textChanges(),
            nameEditText.textChanges(),
            phoneEditText.textChanges()
        ) { surname, name, phone ->
            surname.length >= 2 && surname.isNotBlank()
                    && name.length >= 2 && name.isNotBlank() && phone.isNotBlank()
                    && phone.length == 11 || phone.length == 12
        }.subscribe { enabled ->
            saveButton.isEnabled = enabled
        }.addTo(subscriptions)
    }

    private fun observeSaveButtonClicks() = saveButton.clicks().subscribe { saveInDb() }.addTo(subscriptions)

    private fun observeSimpleDraweeView() {
        rxPermissions = RxPermissions(this)
        profileImage.setOnClickListener {
            rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({
                    if (it) pickImage()
                }, {
                    Timber.e(it)
                }).addTo(subscriptions)
        }
    }

    private fun setSpinnerListener() {
            when (typeContact) {
                getString(R.string.friends) -> {
                    typeSpinner.setItems(resources.getStringArray(R.array.arrays0).asList())
                }
                getString(R.string.colleagues) -> {
                    typeSpinner.setItems(resources.getStringArray(R.array.arrays1).asList())
                }
            }

        typeSpinner.setOnItemSelectedListener { view, position, id, item ->
            when (item as String) {
                getString(R.string.friends) -> {
                    friendsFieldsVisible()
                    typeContact = item
                }
                getString(R.string.colleagues) -> {
                    colleaguesFieldsVisible()
                    typeContact = item
                }
            }
        }
    }

    private fun saveInDb() {
        val url = urlProfile
        val surname = surnameEditText.text.toString()
        val name = nameEditText.text.toString()
        val patronymic = patronymicEditText.text.toString()
        val phone = phoneEditText.text.toString()
        var id = 0
        if(viewModel.phone_contact == "Фамилия"){
             id = viewModel.phone_contact_id.toInt()
        }else{
           id = phone.hashCode()
        }

        val typeInt = if (typeContact == getString(R.string.friends)) 0 else 1

        var newContact: Contact? = null

        when (typeContact) {
            getString(R.string.friends) -> {
                val birthdate = birthdateEditText.text.toString()
                newContact = Contact(
                    id,
                    typeContact,
                    typeInt,
                    url,
                    surname,
                    name,
                    patronymic,
                    phone,
                    birthdate,
                    null,
                    null
                )
            }
            getString(R.string.colleagues) -> {
                val position = positionEditText.text.toString()
                val officePhone = officePhoneEditText.text.toString()
                newContact = Contact(
                    id,
                    typeContact,
                    typeInt,
                    url,
                    surname,
                    name,
                    patronymic,
                    phone,
                    null,
                    position,
                    officePhone
                )
            }
            else -> {
                Timber.tag("--SAMPLE-").i(newContact.toString())
            }
        }

        if (update) {
            getContact()?.let {
                Timber.tag("--CONTACT-deleteContact").i("---КОНЕЦ---" + it.toString())
                viewModel.deleteContact(it.id)
            }
        }
        newContact?.let {
            if(viewModel.phone_contact == "Фамилия"){
                viewModel.saveInPhoneContact(it,context!!.contentResolver)
            }
            viewModel.saveContact(it)
        }
    }

    private fun friendsFieldsVisible() {
        birthdateLayout.visible()
        positionLayout.gone()
        officePhoneLayout.gone()
    }

    private fun colleaguesFieldsVisible() {
        birthdateLayout.gone()
        positionLayout.visible()
        officePhoneLayout.visible()
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PROFILE_IMAGE_REQUEST_CODE)
    }

    private fun showProfile(uri: Uri) {
        profileImage.setImageURI(uri)
    }

    private fun updateUi(typeContact: String) {
        Timber.tag("--SAMPLE--updateUi").i("---------updateUi--------")
        when (typeContact) {
            getString(R.string.friends) -> friendsFieldsVisible()
            getString(R.string.colleagues) -> colleaguesFieldsVisible()
        }
    }

    private fun saveViewsState() {
        viewModel.vmTypeContact = typeContact
        viewModel.vmUrlProfile = urlProfile
        viewModel.vmSurname = surnameEditText.text.toString()
        viewModel.vmName = nameEditText.text.toString()
        viewModel.vmPatronymic = patronymicEditText.text.toString()
        viewModel.vmPhone = phoneEditText.text.toString()
        viewModel.vmBirthdate = birthdateEditText.text.toString()
        viewModel.vmPosition = positionEditText.text.toString()
        viewModel.vm_office_phone = officePhoneEditText.text.toString()
    }

}