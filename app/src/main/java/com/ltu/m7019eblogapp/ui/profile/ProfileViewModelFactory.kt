package com.ltu.m7019eblogapp.ui.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ltu.m7019eblogapp.model.User

// View model has argument -> requires factory
class ProfileViewModelFactory(private val application: Application,
                              private val user: User
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(application, user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}