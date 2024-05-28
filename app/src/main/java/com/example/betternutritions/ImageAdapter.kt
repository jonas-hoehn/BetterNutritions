package com.example.carousel
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.betternutritions.R
import com.google.android.material.carousel.MaskableFrameLayout

class ImageAdapter(var context: Context, var arrayList: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.image_list_item_carousel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(arrayList[position]).into(holder.imageView)
        holder.itemView.setOnClickListener { view: View? ->
            onItemClickListener!!.onClick(
                holder.imageView,
                arrayList[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById<ImageView>(R.id.list_item_image_carousel)
        }
    }

    interface OnItemClickListener {
        fun onClick(imageView: ImageView?, path: String?)
    }

}