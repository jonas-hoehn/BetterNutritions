package com.example.betternutritions

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.betternutritions.databinding.FragmentHomeBinding
import com.example.betternutritions.databinding.FragmentLibraryBinding
import com.example.betternutritions.model.ProductData
import com.example.carousel.ImageAdapter
import com.example.carousel.ImageViewActivity
import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val TAG = "TAG"

    val productEntries: ArrayList<ProductData> = ArrayList()
    val carouselPictures: ArrayList<String> = ArrayList()
    private lateinit var feedAdapter: ArrayAdapter<ProductData>
    private lateinit var recyclerView: RecyclerView

    private lateinit var client: OkHttpClient
    private var jsonString: String = ""
    private val binding get() = _binding!!
    private lateinit var view: View

    private lateinit var verificationCard : CardView
    private lateinit var resendCode : Button

    private lateinit var lBinding: FragmentLibraryBinding

    // https://openfoodfacts.github.io/openfoodfacts-server/api/
    // .org, nicht .net (.net = test environment)
    private val url = "https://world.openfoodfacts.org/api/v3/product/"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        client = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .build()

        resendCode = binding.verifyNowButton
        verificationCard = binding.verificationCard
        val fAuth = FirebaseAuth.getInstance()

        if(fAuth.currentUser?.isEmailVerified == false){
            verificationCard.visibility = View.VISIBLE
            resendCode.setOnClickListener {
                fAuth.currentUser?.sendEmailVerification()
                Toast.makeText(requireContext(), "Verification Email sent again.", Toast.LENGTH_SHORT).show()
            }
        }

        feedAdapter = FeedAdapter(requireContext(), R.layout.list_item_cardview, productEntries)

        recyclerView = binding.recyclerViewCarousel
        carouselPictures.add("https://as1.ftcdn.net/v2/jpg/03/93/35/78/1000_F_393357864_Kt1UzwlVR4r7M5tTqqBQtjpWRDckJLNU.jpg")
        carouselPictures.add("https://img.freepik.com/premium-vector/continuous-line-drawing-man-workout-activities-fitness-concept-isolated-white-background_554735-277.jpg?w=1800")
        carouselPictures.add("https://gymwolf.com/images/exercises/1223_0.jpg")
        val imageAdapter= ImageAdapter(requireContext(), carouselPictures)

        imageAdapter.onItemClickListener = object : ImageAdapter.OnItemClickListener {
            override fun onClick(imageView: ImageView?, path: String?) {
                val intent = Intent(requireContext(), ImageViewActivity::class.java)
                intent.putExtra("image", attr.path)
                startActivity(intent)
            }
        }

/*        val carousel = binding.recyclerViewCarousel
        carousel.setOnClickListener {
            Toast.makeText(requireContext(), "Carousel clicked", Toast.LENGTH_SHORT).show()
        }*/


        recyclerView.adapter = imageAdapter

        lBinding = FragmentLibraryBinding.inflate(layoutInflater)
        val jsonListView = lBinding.jsonListView
        val jsonView: ListView = jsonListView
        jsonView.adapter = feedAdapter

        view = inflater.inflate(R.layout.fragment_home, container, false)

        /*binding.textviewFirst.setOnClickListener { Navigation.findNavController(view).navigate(R.id.navigateToSecondFragment) }*/

        val mainActivity: MainActivity = requireActivity() as MainActivity
        mainActivity?.setCurrentFragmentActivity(this)

        return binding.root
    }

/*    private fun serializeProduct(code: String) {

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
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "NavController")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}