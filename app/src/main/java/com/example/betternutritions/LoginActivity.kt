package com.example.betternutritions

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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {
    var loginEmail: EditText? = null
    var loginPassword: EditText? = null
    private lateinit var loginButton: Button
    private lateinit var signupRedirectText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        val verified = auth.currentUser?.isEmailVerified
        loginEmail = findViewById<EditText>(R.id.login_email)
        loginPassword = findViewById<EditText>(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById<TextView>(R.id.signupRedirectText)
        progressBar = findViewById(R.id.progressBar)


        signupRedirectText.setOnClickListener(View.OnClickListener {
                val intent = Intent(
                    this@LoginActivity,
                    SignupActivity::class.java
                )
                startActivity(intent)
            })


        loginButton.setOnClickListener(View.OnClickListener {

            if (!validateUsername() or !validatePassword()) {
                return@OnClickListener
            }
            if(verified == false){
                Toast.makeText(this, "Please verify your email", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            progressBar.setVisibility(View.VISIBLE)

            //authenticating user

            auth.signInWithEmailAndPassword(loginEmail!!.text.toString(), loginPassword!!.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        progressBar.setVisibility(View.GONE)
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Error ! " + task.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    fun validateUsername(): Boolean {
        val valUser = loginEmail!!.text.toString()
        return if (valUser.isEmpty()) {
            loginEmail!!.error = "Username cannot be empty"
            false
        } else {
            loginEmail!!.error = null
            true
        }
    }

    fun validatePassword(): Boolean {
        val valPwd = loginPassword!!.text.toString()
        return if (valPwd.isEmpty()) {
            loginPassword!!.error = "Password cannot be empty"
            false
        } else {
            loginPassword!!.error = null
            true
        }
    }

    /*fun checkUser() {
        val userUsername = loginUsername!!.text.toString().trim { it <= ' ' }
        val userPassword = loginPassword!!.text.toString().trim { it <= ' ' }
        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(userUsername)
        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    loginUsername!!.error = null
                    val passwordFromDB = snapshot.child(userUsername).child("password").getValue(
                        String::class.java
                    )
                    if (passwordFromDB == userPassword) {
                        loginUsername!!.error = null
                        val nameFromDB = snapshot.child(userUsername).child("name").getValue(
                            String::class.java
                        )
                        val emailFromDB = snapshot.child(userUsername).child("email").getValue(
                            String::class.java
                        )
                        val usernameFromDB =
                            snapshot.child(userUsername).child("username").getValue(
                                String::class.java
                            )
                        val intent = Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        )
                        intent.putExtra("name", nameFromDB)
                        intent.putExtra("email", emailFromDB)
                        intent.putExtra("username", usernameFromDB)
                        intent.putExtra("password", passwordFromDB)
                        startActivity(intent)
                    } else {
                        loginPassword!!.error = "Invalid Credentials"
                        loginPassword!!.requestFocus()
                    }
                } else {
                    loginUsername!!.error = "User does not exist"
                    loginUsername!!.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }*/
}