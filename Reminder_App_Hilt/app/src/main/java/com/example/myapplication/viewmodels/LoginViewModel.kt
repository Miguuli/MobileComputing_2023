package com.example.myapplication.viewmodels

import androidx.lifecycle.*
import com.example.myapplication.tools.NAME_DEFAULT
import com.example.myapplication.tools.PASSWORD_DEFAULT
import com.example.myapplication.tools.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor():
    ViewModel() {
    private val preferences = Preferences()

    fun update_username(value: String?){
        preferences.set_name(value!!)
    }

    fun update_password(value: String?){
        preferences.set_password(value!!)
    }

    fun check_for_user(): Boolean{
        if(preferences.get_name() != NAME_DEFAULT &&
            preferences.get_password() != PASSWORD_DEFAULT){
            println("user has been created")
            return true
        }
        println("user has not been created")
        return false
    }

    fun authorize(name: String?, password: String?): Boolean{
        if(preferences.get_name() == name!! &&
                preferences.get_password() == password!!) {
            return true
        }
        return false
    }
}