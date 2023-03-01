package com.android.jsjstore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.R
import com.android.jsjstore.model.Category
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CategoryAdapter(options: FirebaseRecyclerOptions<Category>) :
    FirebaseRecyclerAdapter<Category, CategoryAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Category) {
        val storeRef: StorageReference =
            FirebaseStorage.getInstance().getReferenceFromUrl(model.image)
        Glide.with(holder.categoryImage.context).load(storeRef).into(holder.categoryImage)

        holder.categoryName.text = model.title
    }

    inner class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.category_viewholder, parent, false)) {

        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)
    }
}