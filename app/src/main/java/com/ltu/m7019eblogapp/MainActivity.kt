package com.ltu.m7019eblogapp

import android.accounts.AccountManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.ltu.m7019eblogapp.databinding.ActivityMainBinding
import com.ltu.m7019eblogapp.model.User
import com.ltu.m7019eblogapp.ui.faq.FaqFragment
import com.ltu.m7019eblogapp.ui.faq.FaqFragmentDirections
import com.ltu.m7019eblogapp.ui.home.HomeFragment
import com.ltu.m7019eblogapp.ui.login.LoginFragment
import com.ltu.m7019eblogapp.ui.login.UserSessionViewModel
import com.ltu.m7019eblogapp.ui.profile.ProfileFragment
import com.ltu.m7019eblogapp.ui.profile.ProfileFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var cachedUser: User? = null

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var account: Auth0
    private lateinit var manager: CredentialsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Auth0 account with client ID and domain from resources
        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        val apiClient = AuthenticationAPIClient(account)
        manager = CredentialsManager(apiClient, SharedPreferencesStorage(this))

        drawerLayout = binding.drawerLayout

        val toolbar = binding.topAppBar
        setSupportActionBar(toolbar)

        val navigationView = binding.navView
        val topAppBar = binding.topAppBar

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_faq, R.id.navigation_profile), drawerLayout)
        topAppBar.setupWithNavController(navController, appBarConfiguration)

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)

        navigationView.setNavigationItemSelectedListener { item ->
            println("PRESSED")

            when (item.itemId) {
                R.id.top_nav_home -> {
                    navController.navigate(R.id.navigation_home)
                }
                R.id.top_nav_faq -> navController.navigate(R.id.navigation_faq)
                R.id.top_nav_profile -> navController.navigate(R.id.navigation_profile)
                R.id.nav_logout -> {
                    doLogout()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Enable UI elements for the logged-in user.
     */
    fun enableUI(user: User){
        binding.navView.visibility = View.VISIBLE
        binding.topAppBar.visibility = View.VISIBLE

        cachedUser = user

        // Load new user data into side drawer
        val userNameView = binding.navView.findViewById<TextView>(R.id.nav_user_name)
        val userEmailView = binding.navView.findViewById<TextView>(R.id.nav_user_email)
        val userPicView = binding.navView.findViewById<ShapeableImageView>(R.id.nav_user_pic)

        userNameView.text = user.username
        userEmailView.text = user.email

        Glide.with(this).load(user.profilePicture).into(userPicView)
    }

    /**
     * Disable UI elements when the user logs out.
     */
    fun disableUI(){
        binding.navView.visibility = View.GONE
        binding.topAppBar.visibility = View.GONE

        cachedUser = null
    }

    /**
     * Initiate AUTH0 logout flow.
     */
    fun doLogout(){
        WebAuthProvider.logout(account).withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        error.getCode()))
                }

                override fun onSuccess(result: Void?) {
                    finalizeLogout()
                }
            })
    }

    /**
     * Finalize logout and reset cache.
     * This method should only be used as a callback from doLogout().
     */
    private fun finalizeLogout(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        manager.clearCredentials()

        disableUI()

        // Navigate to the LoginFragment
        navController.navigate(R.id.navigation_login)
    }

    private fun showSnackBar(text: String) {
        Snackbar
            .make(
                binding.root,
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }
}
