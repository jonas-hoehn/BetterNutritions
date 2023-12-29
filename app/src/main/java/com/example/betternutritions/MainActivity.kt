package com.example.betternutritions

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.betternutritions.databinding.ActivityMainBinding
import com.example.betternutritions.databinding.ContentMainBinding
import com.example.betternutritions.databinding.FragmentHomeBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class MainActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var cBinding: ContentMainBinding
    private lateinit var hBinding: FragmentHomeBinding
    private lateinit var bottomNavigationView: BottomNavigationView


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
                    navigateToHome()
                    if (currentFragment is HomeFragment) {
                        (currentFragment as HomeFragment).apply {
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

        initBinding()
        // DAS Folgende macht alles kaputt!
        //setContentView(R.layout.activity_main)
        setContentView(binding.drawerLayout)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        val drawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )


        val navigationView: NavigationView = findViewById(R.id.nav_view)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            //supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        val barVisibility: Int = binding.bottomAppBar.visibility
        val btnScanVisibility: Int = binding.btnScan.visibility
        binding.btnScan.setOnClickListener { showBottomDialog() }
        showBottomNavigationBar(barVisibility, btnScanVisibility)

        bottomNavigationView = this.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.home -> {
                    navigateToHome()
                }

                R.id.search -> {
                    navigateToSearch()
                }

                R.id.settings -> {
                    navigateToSettings()
                }
            }
            true

        }
        /* ---------------------------------------------------------------------------------------------*/


    }

    private fun navigateToSettings() {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId != R.id.settingsFragment)
            navController.navigate(R.id.navigateToSettingsFragment)
    }

    private fun navigateToSearch() {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId != R.id.searchFragment)
            navController.navigate(R.id.navigateToSearchFragment)
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
        binding.navView.visibility =
            if (barVisibility == 0) BottomAppBar.VISIBLE else BottomAppBar.GONE
        if (fabVisibility == 0) binding.btnScan.show() else binding.btnScan.hide()
    }


    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)
        val scanProduct = dialog.findViewById<LinearLayout>(R.id.scanProduct)
        val shortsLayout = dialog.findViewById<LinearLayout>(R.id.layoutShorts)
        val liveLayout = dialog.findViewById<LinearLayout>(R.id.layoutLive)
        val cancelButton = dialog.findViewById<ImageView>(R.id.cancelButton)

        scanProduct.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this@MainActivity, "Bar-Code Scanner aktiviert", Toast.LENGTH_LONG)
                .show()

            checkPermissionCamera(this)
        }
        shortsLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this@MainActivity, "Create a short is Clicked", Toast.LENGTH_SHORT)
                .show()
        }
        liveLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this@MainActivity, "Go live is Clicked", Toast.LENGTH_SHORT).show()
        }
        cancelButton.setOnClickListener { dialog.dismiss() }
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
        setContentView(binding.drawerLayout)
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun setCurrentFragmentActivity(fragment: Fragment) {
        currentFragment = fragment
    }

}