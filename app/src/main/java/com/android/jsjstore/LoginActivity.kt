package com.android.jsjstore

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.android.jsjstore.databinding.ActivityLoginBinding
import com.android.jsjstore.utils.CommonUtility
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    var isSidebarClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth
        isSidebarClicked = intent.getSerializableExtra("sidebar") as? Boolean ?: false

        setContentView(binding.root)
        navigationMenuBehaviour(binding)

        binding.btnGoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnEmailSignIn.setOnClickListener {
            signInWithEmail()
        }
    }

    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract())
    { result ->
        this.onSignInResult(result)
    }

    private fun signInWithEmail() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            updateUI(user)
        } else {
            signInWithEmail()
        }
    }

    /*Google Authentication Login */
    private fun signInWithGoogle() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("14671755869-9mv5jl2v4lljqjb06347sup9qmp41res.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //sign out previous session if any so that the user can add other account if required
        signOut()

        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.i(ContentValues.TAG, "FirebaseAuth with Google " + account.id);
                // Signed in successfully, show authenticated UI.
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                //Update UI appropriately
                Log.e(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //sign in success, update UI with signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential: success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.e(ContentValues.TAG, "signInWithCredential failure" + task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            updateSharedPreferences(user)
            val intent = if (isSidebarClicked) {
                Intent(applicationContext, HomeActivity::class.java)
            } else {
                Intent(applicationContext, CheckoutActivity::class.java)
            }
            startActivity(intent)
        }
    }

    companion object {
        const val RC_SIGN_IN = 1001
    }

    private fun updateSharedPreferences(user: FirebaseUser) {
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
            "userInfo",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString("userId", user.email.toString())
        editor.putString("displayName", user.displayName)
        editor.apply()
    }

    private fun navigationMenuBehaviour(binding: ActivityLoginBinding) {
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@LoginActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout?.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView?.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.cartPage -> {
                        startActivity(Intent(this@LoginActivity, CartActivity::class.java))
                    }
                    R.id.homePage -> {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    }
                    R.id.orderPage -> {
                        startActivity(Intent(this@LoginActivity, OrderHistoryActivity::class.java))
                    }
                    R.id.loginOrLogoutPage -> {
                        val loggedInUser = CommonUtility.getLoggedInUser(applicationContext)

                        if (loggedInUser == "") {
                            val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                            intent.putExtra("sidebar", true)
                            startActivity(intent)
                        } else {
                            startActivity(Intent(this@LoginActivity, LogoutActivity::class.java))
                        }
                    }
                }
                true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}