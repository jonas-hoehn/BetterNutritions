package com.example.betternutritions

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.betternutritions.model.ProductData

class FeedAdapter(context: Context, val resource: Int, val products: List<ProductData>): ArrayAdapter<ProductData>(context, resource, products) {
    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return products.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d(TAG, "getView() called")
        val view: View
        if(convertView == null){
            Log.d(TAG, "getView() called with null convertView")
            view = inflater.inflate(resource, parent, false)
        }else{
            Log.d(TAG, "getView() provided a converView")
            view = convertView
        }

        val productName: TextView = view.findViewById(R.id.productName)
        val productBrand: TextView = view.findViewById(R.id.productBrand)
        val productNutriscore: TextView = view.findViewById(R.id.productNutriScore)
        val productPicture: ImageView = view.findViewById(R.id.productPicture)

        val currentProduct = products[position]

        productName.text = currentProduct.product.product_name
        productBrand.text = currentProduct.product.brands
        val nutriscore = if (currentProduct.product.nutriscore_score != null) currentProduct.product.nutriscore_score.toString() else  "?"
        productNutriscore.text = "Nutriscore: " + nutriscore + "/100"
        Glide.with(productPicture).load(currentProduct.product.image_url)
            .into(productPicture)

        return view
    }
}