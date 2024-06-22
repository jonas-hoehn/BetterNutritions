package com.example.betternutritions

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.betternutritions.databinding.ActivityMainBinding
import com.example.betternutritions.databinding.ContentMainBinding
import com.example.betternutritions.databinding.FragmentHomeBinding
import com.example.betternutritions.databinding.FragmentLibraryBinding
import com.example.betternutritions.databinding.FragmentSettingsBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var cBinding: ContentMainBinding
    private lateinit var hBinding: FragmentHomeBinding
    private lateinit var sBinding: FragmentSettingsBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var scanningProductsButton: ExtendedFloatingActionButton


    //private val TAG = "TAG"
    //private val number = "CODE"

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showCamera()
            } else {
                TODO()
            }
        }

    private val scanLauncher =
        registerForActivityResult(ScanContract()) {

            result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    navigateToLibrary()
                    if (currentFragment is LibraryFragment) {
                        (currentFragment as LibraryFragment).apply {
                            setContentResult(result.contents)
                        }
                    }
                }
            }

        }



    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setPrompt("Scan QR Code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        initBinding()
        // DAS Folgende macht alles kaputt!
        //setContentView(R.layout.activity_main)
        setContentView(binding.coordinatorLayout)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController



        //setSupportActionBar(binding.toolbar)
        val barVisibility: Int = binding.bottomAppBar.visibility
        val btnScanVisibility: Int = binding.btnScan.visibility
        showBottomNavigationBar(barVisibility, btnScanVisibility)
        val fabMainActivity = binding.btnScan
        fabMainActivity.setOnClickListener { showBottomDialog() }


        bottomNavigationView = this.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.home -> {
                    navigateToHome()
                }

                R.id.library -> {
                    navigateToLibrary()
                }

                R.id.settings -> {
                    navigateToSettings()
                }
            }
            true

        }
        /* ---------------------------------------------------------------------------------------------*/

        scanningProductsButton = findViewById(R.id.scanningProducts)
        scanningProductsButton.setOnClickListener {
            checkPermissionCamera(this)
        }


    }

    private fun navigateToSettings() {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId != R.id.settingsFragment)
            navController.navigate(R.id.navigateToSettingsFragment)
    }


    private fun navigateToLibrary() {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId != R.id.libraryFragment)
            navController.navigate(R.id.navigateToLibraryFragment)
    }

    private fun navigateToHome() {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId != R.id.nav_home)
            navController.navigate(R.id.navigateToHome)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Check if you want to prevent the app from exiting
        super.onBackPressed() // default leaves the app

    }

    private fun showBottomNavigationBar(barVisibility: Int, fabVisibility: Int) {
        binding.bottomNavigationView.visibility =
            if (barVisibility == 0) BottomAppBar.VISIBLE else BottomAppBar.GONE
        if (fabVisibility == 0) binding.btnScan.show() else binding.btnScan.hide()
    }


    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)
        val scanProduct = dialog.findViewById<Button>(R.id.scanProduct)
        val goToTrainings = dialog.findViewById<TextView>(R.id.goToTrainings)

        scanProduct.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this@MainActivity, "Bar-Code Scanner aktiviert", Toast.LENGTH_LONG)
                .show()

            checkPermissionCamera(this)
        }

        goToTrainings.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this@MainActivity, "Trainings", Toast.LENGTH_LONG)
                .show()

        }
       // cancelButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

    /*-----------------------------------------------------------------------------------------------------*/


    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        cBinding = ContentMainBinding.inflate(layoutInflater)
        hBinding = FragmentHomeBinding.inflate(layoutInflater)
        sBinding = FragmentSettingsBinding.inflate(layoutInflater)
        //setContentView(binding.coordinatorLayout)
    }


    private fun checkPermissionCamera(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showCamera()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context, "CAMERA permission required", Toast.LENGTH_LONG).show()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }


/*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }*/


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun setCurrentFragmentActivity(fragment: Fragment) {
        currentFragment = fragment
    }




}