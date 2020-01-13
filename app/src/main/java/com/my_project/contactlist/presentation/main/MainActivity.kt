package com.my_project.contactlist.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.my_project.contactlist.App
import com.my_project.contactlist.R
import com.my_project.contactlist.presentation.change.ChangeFragment
import com.my_project.contactlist.router.Router
import com.my_project.contactlist.router.Screen
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appComponent.inject(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        router.init(supportFragmentManager)
        if (savedInstanceState == null) {
            viewModel.showContacts()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ChangeFragment.PROFILE_IMAGE_REQUEST_CODE) {
            val changeFragment =
                (supportFragmentManager.findFragmentById(R.id.main_container) as? ChangeFragment)
            changeFragment?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() = when (router.actualScreen) {
        Screen.CONTACTS -> super.onBackPressed()
        Screen.CHANGE -> viewModel.back()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        router.detach()
        super.onDestroy()
    }
}
