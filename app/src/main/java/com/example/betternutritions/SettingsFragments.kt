package com.example.betternutritions

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.activity.result.contract.ActivityResultContracts
import com.example.betternutritions.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class SettingsFragments : Fragment() {

    private lateinit var fullName: EditText
    private lateinit var emailAddress: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var changeProfile: Button
    private lateinit var resetPassword: Button
    private lateinit var accountLogout: Button
    private lateinit var tButton: Button

    private var _binding: FragmentSettingsBinding? = null
    private val sBinding get() = _binding!!
    private val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(requireContext(), "SettingsFragment ist in OnCreate()", Toast.LENGTH_SHORT).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        //sBinding = FragmentSettingsBinding.inflate(layoutInflater)

        Toast.makeText(requireContext(), "SettingsFragment ist in OnCreateView()", Toast.LENGTH_SHORT).show()
        fullName = sBinding.profileName
        emailAddress = sBinding.profileEmail
        profileImageView = sBinding.profileImage
        changeProfile = sBinding.changeProfile
        resetPassword = sBinding.resetPassword
        accountLogout = sBinding.logoutProfile
        tButton = sBinding.tButton

        fullName.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "Full name clicked!") // Add for debugging
            Toast.makeText(requireContext(), "Full name clicked!", Toast.LENGTH_SHORT).show()
        })


        accountLogout.setOnClickListener {
            Log.d(TAG, "Logout button clicked!") // Add for debugging
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Logout successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        tButton.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "tButton clicked!") // Add for debugging
            Toast.makeText(requireContext(), "tButton clicked!", Toast.LENGTH_SHORT).show()
        })

        return inflater.inflate(R.layout.fragment_settings, container, false)


    }

    override fun onViewCreated(onViewCreated: View, savedInstanceState: Bundle?) {
        super.onViewCreated(onViewCreated, savedInstanceState)


    }

/*    fun logout(item: SettingsFragments) {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }*/
}