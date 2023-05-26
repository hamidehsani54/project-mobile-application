package com.ltu.m7019eblogapp.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.google.android.material.snackbar.Snackbar
import com.ltu.m7019eblogapp.MainActivity
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.data.util.DataFetchStatus
import com.ltu.m7019eblogapp.databinding.FragmentHomeBinding
import com.ltu.m7019eblogapp.databinding.FragmentLoginBinding
import com.ltu.m7019eblogapp.model.User
import com.ltu.m7019eblogapp.ui.home.HomeFragment
import com.ltu.m7019eblogapp.ui.home.HomeViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var account: Auth0
    private lateinit var manager: CredentialsManager

    private lateinit var viewModel: LoginViewModel

    private var cachedCredentials: Credentials? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Override backaction
        requireActivity().onBackPressedDispatcher.addCallback(this) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        val apiClient = AuthenticationAPIClient(account)
        manager = CredentialsManager(apiClient, SharedPreferencesStorage(this.requireContext()))

        binding.buttonLogin.setOnClickListener {
            binding.buttonLogin.visibility = View.GONE
            binding.textLoadingLogin.visibility = View.VISIBLE
            binding.progressCircularLoadingLogin.visibility = View.VISIBLE
            loginWithBrowser()
        }

        // Observe login-status and perform relevant actions
        viewModel.userFetchStatus.observe(viewLifecycleOwner) { status ->
            status?.let {
                when (it) {
                    DataFetchStatus.LOADING -> {
                        binding.buttonLogin.visibility = View.GONE
                        binding.textLoadingLogin.visibility = View.VISIBLE
                        binding.progressCircularLoadingLogin.visibility = View.VISIBLE
                        println("loading user...")
                    }
                    DataFetchStatus.ERROR -> {
                        val userSession : UserSessionViewModel by activityViewModels()
                        userSession.user = null
                        userSession.accessToken = null

                        binding.buttonLogin.visibility = View.VISIBLE
                        binding.textLoadingLogin.visibility = View.GONE
                        binding.progressCircularLoadingLogin.visibility = View.GONE
                        showSnackBar("Error logging in...")
                    }
                    DataFetchStatus.DONE -> {
                        println("User found, navigating to home")

                        // Initiate session
                        val userSession : UserSessionViewModel by activityViewModels()
                        userSession.user = viewModel.loggedInUser.value!!
                        userSession.accessToken = cachedCredentials!!.accessToken

                        (activity as MainActivity).enableUI(userSession.user!!)
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }
                }
            }
        }

        if(manager.hasValidCredentials()){

            manager.getCredentials(object: Callback<Credentials, CredentialsManagerException> {
                override fun onSuccess(result: Credentials) {
                    // Use credentials
                    cachedCredentials = result
                    viewModel.attachCredentials(result)
                    println("credentials grabbed from storage!")
                    showUserProfile()
                }

                override fun onFailure(error: CredentialsManagerException) {
                    // No credentials were previously saved or they couldn't be refreshed
                    showSnackBar("Failed to refresh credentials, redirecting...")
                    loginWithBrowser()
                }
            })

        } else {
            loginWithBrowser()
        }


        return root

    }

    //TODO: Move to util
    private fun loginWithBrowser(){
        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope(getString(R.string.login_scopes))
            .withAudience(getString(R.string.login_audience, getString(R.string.com_auth0_domain)))
            .start(this.requireContext(), object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(getString(R.string.login_failure_message, error.getCode()))
                    println(getString(R.string.login_failure_message, error.getCode()))
                    viewModel.commitError()
                }

                override fun onSuccess(result: Credentials) {
                    cachedCredentials = result
                    manager.saveCredentials(result)
                    viewModel.attachCredentials(result)

                    showSnackBar(getString(R.string.login_success_message, result.accessToken))
                    println(getString(R.string.login_success_message, result.accessToken))
                    showUserProfile()

                }
            })
    }

    //TODO: Move to util
    private fun showUserProfile() {
        // Guard against showing the profile when no user is logged in
        if (cachedCredentials == null) {
            showSnackBar("No credentials available for user grab, redirecting...")
            loginWithBrowser()
            return
        }

        val client = AuthenticationAPIClient(account)
        client
            .userInfo(cachedCredentials!!.accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        error.getCode()))
                }

                override fun onSuccess(result: UserProfile) {
                    println("User profile grabbed!")
                    viewModel.attachUserProfile(result)
                    viewModel.finalizeLogin()
                }

            })
    }

    private fun showSnackBar(text: String) {
        Snackbar
            .make(
                this.requireView(),
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }

}