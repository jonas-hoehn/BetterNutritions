package com.example.betternutritions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.betternutritions.databinding.ActivityMainBinding
import com.example.betternutritions.databinding.ContentMainBinding
import com.example.betternutritions.databinding.FragmentFirstBinding
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
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var cBinding: ContentMainBinding
    private lateinit var fBinding: FragmentFirstBinding

    private lateinit var client: OkHttpClient
    private var jsonString: String = ""

    val productEntries: ArrayList<ProductData> = ArrayList()
    private lateinit var feedAdapter: ArrayAdapter<ProductData>
    private val TAG = "TAG"
    private val number = "CODE"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fContext get() = requireContext()

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
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    setResult(result.contents)
                }
            }

        }

    private fun setResult(code: String) {

        Log.d(number, "Code: $code")
        val eanFormatRegex = Regex("^\\d{8}|\\d{13}$")
        val bonareaFormat = Regex("^00\\d{18}$")
        val ucpaFormat = Regex("^[0-9]{12}\$")

        if (!code.matches(eanFormatRegex) && !code.matches(bonareaFormat) && !code.matches(ucpaFormat)) {
            Toast.makeText(context, "Nicht unterstützter Code gescannt: $code", Toast.LENGTH_LONG)
                .show()
        } else {
            // Alternative URL could be "https://world.openfoodfacts.org/api/v3/product/${code}"

            serializeProduct(code)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        client = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .build()
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        initBinding()

        /*arrayAdapter = ArrayAdapter<ProductData>(this, R.layout.list_item, productEntries)
        val jsonView: ListView = findViewById(R.id.jsonListView)
        jsonView.adapter = arrayAdapter*/

        feedAdapter = FeedAdapter(fContext, R.layout.list_record, productEntries)
        val jsonView: ListView = binding.jsonListView
        jsonView.adapter = feedAdapter

        binding.btnScan2.setOnClickListener { view ->
            Snackbar.make(view, "Bar-Code Scanner aktiviert", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            checkPermissionCamera(fContext)
        }


        return binding.root

    }

     fun checkPermissionCamera(context: Context) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView1.setOnClickListener{Navigation.findNavController(view).navigate(R.id.navigateToSecondFragment)}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }

    private fun serializeProduct(code: String) {

        val url = "https://world.openfoodfacts.net/api/v3/product/${code}"
        val request = Request.Builder()
            .url(url)
            .build()

        Log.d(TAG, url)
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
                    activity?.runOnUiThread() {
                        /*val imageView = findViewById<ImageView>(R.id.productImage)
                        val scanResultView = findViewById<TextView>(R.id.scanResultView)
                        Log.d(TAG, "setting text")
                        scanResultView.text = products.product.product_name
                        Glide.with(imageView).load(products.product.image_front_small_url)
                            .into(imageView)*/

                        if (products.status != "failure") {
                            productEntries.add(products)
                            Log.d(TAG, "Liste befüllt")
                            Log.d(TAG, products.toString())

                            //adapter.notifyDataSetChanged()
                            feedAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(fContext, "Keine Produktinformation für den Barcode ${code} vorhanden.", Toast.LENGTH_SHORT).show()
                        }

                    }
                }.start()

            }
        })
    }

    private fun initBinding() {

        //binding = FragmentFirstBinding.inflate(layoutInflater)
        cBinding = ContentMainBinding.inflate(layoutInflater)
        fBinding = FragmentFirstBinding.inflate(layoutInflater)
    }
}