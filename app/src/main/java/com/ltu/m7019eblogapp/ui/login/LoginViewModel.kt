package com.ltu.m7019eblogapp.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.ltu.m7019eblogapp.data.DefaultAppContainer
import com.ltu.m7019eblogapp.data.util.DataFetchStatus
import com.ltu.m7019eblogapp.model.User
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _container: DefaultAppContainer = DefaultAppContainer()
    private var cachedUserProfile: UserProfile? = null
    private var cachedCredentials: Credentials? = null

    private val _userFetchStatus = MutableLiveData<DataFetchStatus>()
    val userFetchStatus: LiveData<DataFetchStatus>
        get() {
            return _userFetchStatus
        }

    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User>
        get() {
            return _loggedInUser
        }

    init {
        _userFetchStatus.value = DataFetchStatus.LOADING
    }

    // Function to log in the user
    private fun loginUser() {
        viewModelScope.launch {
            try {
                // Call the login function in the repository and set the logged-in user
                _loggedInUser.value = _container.blogRepository.login(
                    cachedCredentials!!.accessToken,
                    cachedUserProfile!!.name!!
                )
                _userFetchStatus.value = DataFetchStatus.DONE
                println("--------------------------------------- USER LOGGED IN! ----------------------------------------------")
            } catch (e: Exception) {
                println("!! ERROR LOGGING IN USER !!")
                println(e.message.toString())
                _userFetchStatus.value = DataFetchStatus.ERROR
            }
        }
    }

    // Function to attach the user credentials
    fun attachCredentials(credentials: Credentials) {
        cachedCredentials = credentials
    }

    // Function to attach the user profile
    fun attachUserProfile(userProfile: UserProfile) {
        cachedUserProfile = userProfile
    }

    // Function to finalize the login process
    fun finalizeLogin() {
        if (cachedCredentials != null && cachedUserProfile != null) {
            // If both credentials and user profile are available, proceed with login
            loginUser()
        } else {
            println("Could not finalize login!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            println("credentials: $cachedCredentials")
            println("profile: $cachedUserProfile")
            _userFetchStatus.value = DataFetchStatus.ERROR
        }
    }

    // Function to handle login errors
    fun commitError() {
        _userFetchStatus.value = DataFetchStatus.ERROR
    }

}
