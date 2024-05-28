package com.example.betternutritions

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.red
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class SignupActivity : AppCompatActivity() {
    private lateinit var signupName: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var loginRedirectText: TextView
    private lateinit var signupButton: Button
    private lateinit var progressBar: ProgressBar
    var database: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        signupName = findViewById<EditText>(R.id.signup_name)
        signupEmail = findViewById<EditText>(R.id.signup_email)
        signupUsername = findViewById<EditText>(R.id.signup_username)
        signupPassword = findViewById<EditText>(R.id.signup_password)
        loginRedirectText = findViewById<TextView>(R.id.loginRedirectText)
        signupButton = findViewById(R.id.signup_button)
        progressBar = findViewById(R.id.progressBar)

        signupButton.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE

            signUp()

        })


        loginRedirectText.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                this@SignupActivity,
                LoginActivity::class.java
            )
            startActivity(intent)
        })
    }

    fun signUp(){

        database = FirebaseDatabase.getInstance()
        reference = database!!.getReference("users")
        val name = signupName.getText().toString()
        val email = signupEmail.getText().toString()
        val username = signupUsername.getText().toString()
        val password = signupPassword.getText().toString()
        val helperClass = HelperClass(name, email, username, password)
        reference!!.child(username).setValue(helperClass)

        if(TextUtils.isEmpty(email)){
            signupEmail.setError("Email is Required.")
            return
        }

        if(TextUtils.isEmpty(password)){
            signupPassword.setError("Password is Required.")
            return
        }

        if(password.length < 6){
            signupPassword.setError("Password Must be at least Characters")
            return
        }

        progressBar.setVisibility(View.VISIBLE)

        //register the user in firebase

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "User Created.", Toast.LENGTH_LONG).show()
                    val user = auth.currentUser

                    //store user data in firebase real time database
                    val fUser = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "username" to username,
                        "password" to password
                    )

                    db.collection("users").add(fUser)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                    //send verification link
                    if (user != null) {
                        user.sendEmailVerification()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Verification Email Has been Sent.", Toast.LENGTH_LONG).show()
                            }
                    }



                    Toast.makeText(this, "User Created.", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MainActivity ::class.java))
                } else {
                    Toast.makeText(this, "Error ! " + task.exception!!.message, Toast.LENGTH_LONG).show()
                    progressBar.setVisibility(View.GONE)
                }
            }

    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}