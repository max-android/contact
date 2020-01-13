package com.my_project.contactlist.router

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.my_project.contactlist.R
import com.my_project.contactlist.data.entities.Contact
import com.my_project.contactlist.presentation.change.ChangeFragment
import com.my_project.contactlist.presentation.contacts.ContactsFragment

class Router {

    private var fragmentManager: FragmentManager? = null
    var actualScreen: Screen = Screen.CONTACTS

    fun init(fragmentManager: FragmentManager?) {
        this.fragmentManager = fragmentManager
    }

    fun replace(screen: Screen,type:Boolean = false, data: Any = Any()) {
        applyCommand(screen, Command.REPLACE,type, data)
    }

    fun forward(screen: Screen, type:Boolean = false, data: Any = Any()) {
        applyCommand(screen, Command.FORWARD, type, data)
    }

    fun detach() {
        fragmentManager = null
    }

    fun back() {
        fragmentManager?.popBackStack()
        updateActualBackScreen()
    }

    private fun applyCommand(screen: Screen, command: Command, type: Boolean, data: Any) {
        updateActualScreen(screen)
        doFragmentTransaction(screen, command,type, data)
    }

    private fun updateActualScreen(screen: Screen) {
        actualScreen = screen
    }

    private fun doFragmentTransaction(screen: Screen, command: Command, type: Boolean, data: Any) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.main_container, getFragment(screen,type, data))
            ?.apply { if (command == Command.FORWARD) addToBackStack(screen.name) }
            ?.setCustomAnimations(
                R.animator.slide_in_right,
                R.animator.slide_out_left,
                R.animator.slide_in_left,
                R.animator.slide_out_right
            )
            ?.commitAllowingStateLoss()
    }

    private fun getFragment(screen: Screen,type:Boolean, data: Any): Fragment {
        return when (screen) {
            Screen.CONTACTS -> ContactsFragment.newInstance()
            Screen.CHANGE -> ChangeFragment.newInstance(type,data as? Contact)
        }
    }

    private fun updateActualBackScreen() {
        actualScreen = when (actualScreen) {
            Screen.CHANGE -> Screen.CONTACTS
            else -> Screen.CONTACTS
        }
    }
}