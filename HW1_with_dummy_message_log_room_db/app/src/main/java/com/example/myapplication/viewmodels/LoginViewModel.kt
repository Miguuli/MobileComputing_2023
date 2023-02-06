package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.myapplication.data.entity.User
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.tools.NAME_DEFAULT
import com.example.myapplication.tools.PASSWORD_DEFAULT
import com.example.myapplication.tools.Preferences
import com.example.myapplication.ui.Graph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class LoginViewModel(private val app: Application,
                     private val userRepository: UserRepository
                        = Graph.userRepository): ViewModel() {
    private val preferences = Preferences(app)
    private val my_job = SupervisorJob()
    private val my_scope = CoroutineScope(my_job + Dispatchers.Main)

    fun update_username(value: String?){
        preferences.set_name(value!!)

        my_scope.launch {
            userRepository.addUser(User(
                uid = 0,
                firstName = "Mika",
                lastName = "Ihamäki"
            ))
        }
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