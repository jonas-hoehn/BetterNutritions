package com.example.betternutritions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.betternutritions.databinding.FragmentHomeBinding
import com.example.betternutritions.model.ProductData
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val TAG = "TAG"

    val productEntries: ArrayList<ProductData> = ArrayList()
    private lateinit var feedAdapter: ArrayAdapter<ProductData>

    private lateinit var client: OkHttpClient
    private var jsonString: String = ""
    private val binding get() = _binding!!
    private lateinit var view: View

   // private var navHostFragment = parentFragmentManager.findFragmentById(R.id.nav_fragment_content_main) as NavHostFragment?
    // private var navController = navHostFragment?.navController


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        client = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .build()


        feedAdapter = FeedAdapter(requireContext(), R.layout.list_item_cardview, productEntries)
        val jsonView: ListView = binding.jsonListView
        jsonView.adapter = feedAdapter

        view = inflater.inflate(R.layout.fragment_home, container, false)

        /*binding.textviewFirst.setOnClickListener { Navigation.findNavController(view).navigate(R.id.navigateToSecondFragment) }*/

        return binding.root
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
                    activity?.runOnUiThread {
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
                            Toast.makeText(requireContext(), "Keine Produktinformation für den Barcode ${code} vorhanden.", Toast.LENGTH_SHORT).show()
                        }

                    }
                }.start()

            }
        })
    }

     fun setContentResult(code: String) {

        Log.d(TAG, "Code: $code")
        val eanFormatRegex = Regex("^\\d{8}|\\d{13}$")
        val bonareaFormat = Regex("^00\\d{18}$")
        val ucpaFormat = Regex("^[0-9]{12}\$")

        if (!code.matches(eanFormatRegex) && !code.matches(bonareaFormat) && !code.matches(ucpaFormat)) {
            Toast.makeText(requireContext(), "Nicht unterstützter Code gescannt: $code", Toast.LENGTH_LONG)
                .show()
        } else {
            // Alternative URL could be "https://world.openfoodfacts.org/api/v3/product/${code}"

            serializeProduct(code)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val navController = findNavController()

        view.findViewById<ListView>(R.id.jsonListView).onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val productData: ProductData = parent.getItemAtPosition(position) as ProductData
            val selectedItem = productData.toString()
            Log.d(TAG, "Liste $selectedItem")
            Toast.makeText(requireContext(), "Clicked: $selectedItem", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<TextView>(R.id.textview_first).setOnClickListener {
            Log.d(TAG, "Nummer gedrückt")
            //findNavController().navigate(R.id.navigateToSecondFragment)
            }

        view.findViewById<Button>(R.id.btn_next_page).setOnClickListener {
           
        }
        
        Log.d(TAG, "NavController")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}