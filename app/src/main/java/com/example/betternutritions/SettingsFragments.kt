package com.example.betternutritions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.betternutritions.databinding.FragmentSettingsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class SettingsFragments : Fragment() {

    private lateinit var fullName: EditText
    private lateinit var emailAddress: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var changeProfile: Button
    private lateinit var resetPassword: Button
    private lateinit var accountLogout: Button
    private lateinit var tButton: Button

    private lateinit var auth: FirebaseAuth
    private var uid: String? = null
    private lateinit var fStore: FirebaseFirestore
    private var db = Firebase.firestore
    var database: FirebaseDatabase? = null
    var reference: DatabaseReference? = null
    var verified: Boolean? = null

    private var _binding: FragmentSettingsBinding? = null
    private val sBinding get() = _binding!!
    private val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = FragmentSettingsBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid
        verified = auth.currentUser?.isEmailVerified
        database = FirebaseDatabase.getInstance()
        reference = database!!.getReference("users")


    }

    private fun setProfileDataInLayout(name: String?, email: String?) {
        sBinding.profileName.isEnabled = false
        sBinding.profileName.setText(name)
        sBinding.profileEmail.isEnabled = false
        sBinding.profileEmail.setText(email)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(layoutInflater)

        Toast.makeText(requireContext(), "SettingsFragment ist in OnCreateView()", Toast.LENGTH_SHORT).show()
        fullName = sBinding.profileName
        emailAddress = sBinding.profileEmail
        profileImageView = sBinding.profileImage
        changeProfile = sBinding.changeProfile
        resetPassword = sBinding.resetPassword
        accountLogout = sBinding.logoutProfile
        tButton = sBinding.tButton

        fStore = FirebaseFirestore.getInstance()



        tButton.setOnClickListener{

            Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show()



/*            val docRef = db.collection("users").document(uid!!)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        val name = document.getString("name")
                        val email = document.getString("email")
                        setProfileDataInLayout(name, email)
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }*/
        }


        return inflater.inflate(R.layout.fragment_settings, container, false)


    }

    override fun onViewCreated(onViewCreated: View, savedInstanceState: Bundle?) {
        super.onViewCreated(onViewCreated, savedInstanceState)

        changeProfile.setOnClickListener {
            Toast.makeText(context, "Change Profile Clicked", Toast.LENGTH_SHORT).show()
        }

        resetPassword.setOnClickListener {
            Toast.makeText(context, "Reset Password Clicked", Toast.LENGTH_SHORT).show()
        }

        accountLogout.setOnClickListener {
            Toast.makeText(context, "Logout Clicked", Toast.LENGTH_SHORT).show()
        }
    }

/*    fun logout(item: SettingsFragments) {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }*/
}