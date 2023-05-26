package com.ltu.m7019eblogapp.ui.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ltu.m7019eblogapp.model.User

class ProfileViewModel(application: Application, user: User) : ViewModel() {

    // MutableLiveData to hold the text value for the profile fragment
    private val _text = MutableLiveData<String>().apply {
        value = "This is the profile fragment"
    }

    // Expose the text as LiveData to observe changes
    val text: LiveData<String> = _text

    init {
        // Initialize the text value with a greeting message using the user's username
        _text.value = "Hello ${user.username}!"
    }
}
