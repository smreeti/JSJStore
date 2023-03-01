package com.android.jsjstore.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.ProductDetailActivity
import com.android.jsjstore.R
import com.android.jsjstore.model.Product
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductAdapter(options: FirebaseRecyclerOptions<Product>) :
    FirebaseRecyclerAdapter<Product, ProductAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Product) {
        val storeRef: StorageReference =
            FirebaseStorage.getInstance().getReferenceFromUrl(model.picture)
        Glide.with(holder.productImage.context).load(storeRef).into(holder.productImage)

        holder.productTitle.text = model.title
        holder.productPrice.text = model.price.toString()

        holder.btnAdd.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailActivity::class.java)
//            intent.putExtra(
//                "object",
//                productsDomain.get(holder.adapterPosition) as Serializable
//            )
            holder.itemView.context.startActivity(intent)
        }
    }

    inner class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recommended_viewholder, parent, false)) {

        val productTitle: TextView = itemView.findViewById(R.id.productTitle)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val btnAdd: ImageView = itemView.findViewById(R.id.btnAdd)
    }
}