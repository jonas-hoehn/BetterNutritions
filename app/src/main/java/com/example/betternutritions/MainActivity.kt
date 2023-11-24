package com.example.betternutritions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.Glide
import com.example.betternutritions.databinding.ActivityMainBinding
import com.example.betternutritions.databinding.ContentMainBinding
import com.example.betternutritions.model.ProductData
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import okhttp3.Call

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var cBinding: ContentMainBinding

    private val client = OkHttpClient()
    private var jsonString: String = ""

    private val TAG = "TAG"
    private val number = "CODE"

    val productEntries: ArrayList<ProductData> = ArrayList()
    //private lateinit var adapter: ArrayAdapter<ProductData>
    private lateinit var feedAdapter: ArrayAdapter<ProductData>


    //private var navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment_content_main) as NavHostFragment?
    //private var navController = navHostFragment?.navController

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showCamera()
            } else {
                TODO()
            }
        }

    private val scanLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    setResult(result.contents)
                }
            }

        }

    private fun setResult(code: String) {

        Log.d(number, "Code: $code")
        val eanFormatRegex = Regex("^\\d{8}|\\d{13}$")
        val bonareaFormat = Regex("^00\\d{18}$")

        if (!code.matches(eanFormatRegex) && !code.matches(bonareaFormat)) {
            Toast.makeText(this, "Nicht unterstützter Code gescannt: $code", Toast.LENGTH_LONG)
                .show()
        } else {
            val url = "https://world.openfoodfacts.net/api/v3/product/${code}"
            serializeProduct(url)
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
        setContentView(R.layout.activity_main)

        initBinding()
        setSupportActionBar(binding.toolbar)

        /*arrayAdapter = ArrayAdapter<ProductData>(this, R.layout.list_item, productEntries)
        val jsonView: ListView = findViewById(R.id.jsonListView)
        jsonView.adapter = arrayAdapter*/

        feedAdapter = FeedAdapter(this, R.layout.list_record, productEntries)
        val jsonView: ListView = findViewById(R.id.jsonListView)
        jsonView.adapter = feedAdapter

        binding.btnScan.setOnClickListener { view ->
            Snackbar.make(view, "Bar-Code Scanner aktiviert", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            checkPermissionCamera(this)
        }

        binding.btnNextFragment.setOnClickListener { view ->
            Snackbar.make(view, "Auf zur nächsten Seite -> Fragment 1", Snackbar.LENGTH_LONG).show()
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_fragment_content_main, FirstFragment()).commit()
        }
    }

    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cBinding = ContentMainBinding.inflate(layoutInflater)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun serializeProduct(url: String) {

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                jsonString = response.body()?.string().toString()

                val gsonBuilder = GsonBuilder()
                gsonBuilder.setLenient()
                val gson = gsonBuilder.create()

                val products = gson.fromJson(jsonString, ProductData::class.java)
                Log.d(TAG, "Produkt serialisiert")
                println(products)

                Thread {
                    runOnUiThread {
                        /*val imageView = findViewById<ImageView>(R.id.productImage)
                        val scanResultView = findViewById<TextView>(R.id.scanResultView)
                        Log.d(TAG, "setting text")
                        scanResultView.text = products.product.product_name
                        Glide.with(imageView).load(products.product.image_front_small_url)
                            .into(imageView)*/

                        productEntries.add(products)
                        Log.d(TAG, "Liste befüllt")

                        //adapter.notifyDataSetChanged()
                        feedAdapter.notifyDataSetChanged()

                    }
                }.start()

            }
        })
    }


}