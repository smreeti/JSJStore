package com.android.jsjstore

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.jsjstore.model.Category
import com.android.jsjstore.model.Product
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    //private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<ConstraintLayout>(R.id.btnStart)
        auth = Firebase.auth

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        /*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("14671755869-9mv5jl2v4lljqjb06347sup9qmp41res.apps.googleusercontent.com")
            .requestEmail()
            .build()
         */

        // Build a GoogleSignInClient with the options specified by gso.
        //googleSignInClient = GoogleSignIn.getClient(this, gso);

        //sign out previous session if any so that the user can add other account if required
        //signOut()
        btnStart.setOnClickListener {
            //signIn()
            navigateToHomePage()
        }

        initializeCategories()
        initializeProducts()
    }

    /*private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }*/

    private fun navigateToHomePage() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.putExtra(USER_NAME, "")
        startActivity(intent);
    }

    /*private fun signOut() {
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut()

    }*/

    private fun initializeCategories() {
        FirebaseDatabase.getInstance().getReference("categories").removeValue()
        dbRef = FirebaseDatabase.getInstance().getReference("categories");

        val categories: List<Category> = listOf(
            Category("Casual", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"),
            Category("Running", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"),
            Category("Walking", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"),
            Category("Basketball", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"),
            Category("Lifestyle", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"),
            Category("Football", "gs://jsjstore-4e7ce.appspot.com/running_cat.png")
        )

        for (category in categories) {
            dbRef.child(dbRef.push().key!!).setValue(category)
                .addOnCompleteListener {
                    Log.i("CATEGORY DATA INSERTION", "Categories inserted successfully");
                }
        }
    }

    private fun initializeProducts() {
        FirebaseDatabase.getInstance().getReference("products").removeValue()
        dbRef = FirebaseDatabase.getInstance().getReference("products");
        val products: List<Product> = listOf(
            Product(
                productId = 1,
                "Nike Air Max",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "helps keeps your legs fresh late into the fourth quarter for better individual performances and team wins. Sneakers such as the Nike Air Zoom G.T. Run stacks a React footbed on top of a React midsole and Zoom Air in the forefoot to provide shock absorption and a responsive ride.",
                130.99,
                5,
                "Nike",
                "nike_air_max"
            ),
            Product(
                productId = 2,
                "Nike Jordan",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "The Jordan' Why Not?' line proves that speed is the most important factor on the court. With two stacked Zoom Air units in the forefoot for responsive cushioning, they help you harness more of your own speed to change the game.",
                179.99,
                4,
                "Food",
                "nike_jordan"
            ),
            Product(
                productId = 3,
                "Air Jordan Nike",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "This shoes are all about speed and power. They have a full-length Zoom Air unit and Max Air in the heel to absorb impact and return energy. And the KnitPosite 2.0 upper provides a secure fit to help you move with confidence.",
                528.99,
                4,
                "Food",
                "air_jordan"
            ),
            Product(
                productId = 4,
                "Basketball Nike",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "Nike KD basketball shoes have a durable upper that uses minimal material for a broken-in feel. Full-length Zoom Air cushioning",
                310.99,
                4,
                "Food",
                "basketball_nike"
            ),
            Product(
                productId = 5,
                "Nike AIR Jordan 6 Retro",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "Designed by the legendary Tinker Hatfield, the sixth Air Jordan edition had everything fans loved previous models for.",
                799.99,
                4,
                "Food",
                "red_black_nike"
            ),
            Product(
                productId = 6,
                "Nike Blazers",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "Nike Women's W Blazer Low LX Red Stardust Sneakers, Rubber sole, Leather upper for durability and premium comfort, Autoclave construction fuses the outsole to the midsole for a streamlined look, Rubber outsole in a herringbone pattern for traction and durability.",
                499.99,
                4,
                "Food",
                "nike_blazer"
            ),
            Product(
                productId = 7,
                "Nike Classic",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "The Nike Classic Cortez Leather is the original Nike running shoe perfected by an innovative, ultra-soft cushioning system. The shoe is made with a premium leather upper for comfort and durability and with the EVA wedge midsole for lightweight cushioning. Rubber sole provides unbeatable traction and resistance.",
                99.99,
                4,
                "Food",
                "nike_classic"
            )
        )

        for (product in products) {
            dbRef.child(dbRef.push().key!!).setValue(product)
                .addOnCompleteListener {
                    Log.i("PRODUCTS DATA INSERTION ::::::", "Products inserted successfully");
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        /*if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.i(ContentValues.TAG, "FirebaseAuth with Google " + account.id);
                // Signed in successfully, show authenticated UI.
                //firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                //Update UI appropriately
                Log.e(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            }
        }*/
    }

    /*private fun firebaseAuthWithGoogle(idToken: String) {
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
    }*/

    /*private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.putExtra(USER_NAME, user.displayName)
            startActivity(intent);
        }
    }*/

    companion object {
        const val RC_SIGN_IN = 1001
        const val USER_NAME = "USER_NAME"
    }
}