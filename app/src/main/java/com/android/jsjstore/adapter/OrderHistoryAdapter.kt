package com.android.jsjstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.R
import com.android.jsjstore.model.OrderInfo
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class OrderHistoryAdapter(private val orders: MutableList<OrderInfo>) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orderHistoryImage: ImageView = itemView.findViewById(R.id.orderHistoryImage)
        val txtProductOrderTitle: TextView = itemView.findViewById(R.id.txtProductOrderTitle)
        val txtOrderQuantity: TextView = itemView.findViewById(R.id.txtOrderQuantity)
        val txtOrderUnitPrice: TextView = itemView.findViewById(R.id.txtOrderUnitPrice)
        val txtOrderHistoryTotal: TextView = itemView.findViewById(R.id.txtOrderHistoryTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_history_viewholder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]

        val storeRef: StorageReference =
            FirebaseStorage.getInstance().getReferenceFromUrl(order.productImage)
        Glide.with(holder.orderHistoryImage.context).load(storeRef).into(holder.orderHistoryImage)

        holder.txtProductOrderTitle.text = order.productName
        holder.txtOrderQuantity.text = order.quantity.toString()
        holder.txtOrderUnitPrice.text = order.price.toString()
        holder.txtOrderHistoryTotal.text = (order.price * order.quantity).toString()
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}