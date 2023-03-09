package com.android.jsjstore.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.CartActivity
import com.android.jsjstore.ProductDetailActivity
import com.android.jsjstore.R
import com.android.jsjstore.helper.AppDatabase
import com.android.jsjstore.model.ClientOrder
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ClientOrderAdapter(private val orders: List<ClientOrder>) :
    RecyclerView.Adapter<ClientOrderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtproductTitle);
        val pic: ImageView = itemView.findViewById(R.id.imageView4);
        val feeEachItem: TextView = itemView.findViewById(R.id.txtFeeEachItem);
        val totalEachItem: TextView = itemView.findViewById(R.id.txtTotalEachItem);
        val num: TextView = itemView.findViewById(R.id.txtNumberItem);
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDeleteProductCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_viewholder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.title.text = order.productName

        holder.totalEachItem.text = "Total $:" + String.format("%.2f", order.quantity * order.price)
        holder.num.text = "Quantity:${order.quantity.toString()}"
        holder.feeEachItem.text = "Unit Price\$:${String.format("%.2f", order.price)}"

        val storeRef: StorageReference =
            FirebaseStorage.getInstance().getReferenceFromUrl(order?.productImage.toString())
        Glide.with(holder.pic.context).load(storeRef).into(holder.pic)

        holder.btnDelete.setOnClickListener {

            val database = AppDatabase.getInstance(holder.itemView.context)
            GlobalScope.launch {
                database.clientOrderDao().delete(order)
            }
            val intent = Intent(holder.itemView.context, CartActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
