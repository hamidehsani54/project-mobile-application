package com.ltu.m7019eblogapp.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ltu.m7019eblogapp.model.User

class UserSessionViewModel : ViewModel() {
    var user : User? = null
    var accessToken : String? = null
}