package com.example.betternutritions

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.betternutritions.model.ProductData
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LibraryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LibraryFragment : Fragment() {
    val productEntries: ArrayList<ProductData> = ArrayList()
    private lateinit var feedAdapter: ArrayAdapter<ProductData>

    private lateinit var client: OkHttpClient
    private var jsonString: String = ""

    private val url = "https://world.openfoodfacts.org/api/v3/product/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

/*        client = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .build()


        feedAdapter = FeedAdapter(requireContext(), R.layout.list_item_cardview, productEntries)*/



        val mainActivity: MainActivity = requireActivity() as MainActivity
        mainActivity?.setCurrentFragmentActivity(this)
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    private fun serializeProduct(code: String) {

        Log.d(TAG, url+ code)
        val request = Request.Builder()
            .url(url+ code)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                jsonString = response.body()?.string().toString()

                if(response.code() != 200){
                    throw Error("Wrong Code")
                }

                val gsonBuilder = GsonBuilder()
                gsonBuilder.setLenient()
                val gson = gsonBuilder.create()

                val products = gson.fromJson(jsonString, ProductData::class.java)
                Log.d(TAG, "Produkt serialisiert")
                println(products)

                Thread {
                    activity?.runOnUiThread {

                        if (products.status != "failure") {
                            productEntries.add(products)
                            Log.d(TAG, "Liste befüllt")
                            Log.d(TAG, products.toString())

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
            serializeProduct(code)
        }
    }

    fun addProductToLibrary(code: String) {

        Log.d(TAG, "Code: $code")
        val eanFormatRegex = Regex("^\\d{8}|\\d{13}$")
        val bonareaFormat = Regex("^00\\d{18}$")
        val ucpaFormat = Regex("^[0-9]{12}\$")

        if (!code.matches(eanFormatRegex) && !code.matches(bonareaFormat) && !code.matches(ucpaFormat)) {
            Toast.makeText(requireContext(), "Nicht unterstützter Code gescannt: $code", Toast.LENGTH_LONG)
                .show()
        } else {
            serializeProduct(code)
        }
    }
}