package com.example.myapplication.tools


import android.content.Context.MODE_PRIVATE
import com.example.myapplication.Graph

var NAME_DEFAULT = "username"
var PASSWORD_DEFAULT = "password"

class Preferences {
    private val SHARED_PREFS = "com.example.myapplication"
    private val NAME_KEY = "name"
    private val PASSWORD_KEY = "password"
    private val preference = Graph.appContext.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
    private val editor = preference.edit()

    fun set_name(value: String) = editor.putString(NAME_KEY, value).apply()
    fun get_name() = preference.getString(NAME_KEY, NAME_DEFAULT)

    fun set_password(value: String) = editor.putString(PASSWORD_KEY, value).apply()
    fun get_password() = preference.getString(PASSWORD_KEY, PASSWORD_DEFAULT)
}