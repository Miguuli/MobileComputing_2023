package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.myapplication.tools.NAME_DEFAULT
import com.example.myapplication.tools.PASSWORD_DEFAULT
import com.example.myapplication.tools.Preferences

class LoginViewModel(private val app: Application): ViewModel() {
    private val preferences = Preferences(app)

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

class LoginViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(app) as T
    }
}