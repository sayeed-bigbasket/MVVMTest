package com.example.mvvmtest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmtest.R
import com.example.mvvmtest.models.ProductListItem

class ProdAdapter(private val prodList: List<ProductListItem>): RecyclerView.Adapter<ProdAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return prodList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = prodList[position]
        holder.name.text = item.title
        Glide.with(holder.img.context).load(item.image).into(holder.img)
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.iv_prod)
        val name = itemView.findViewById<TextView>(R.id.tv_prod_name)
    }
}