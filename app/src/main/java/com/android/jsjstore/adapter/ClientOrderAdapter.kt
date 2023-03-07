package com.android.jsjstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.R
import com.android.jsjstore.model.ClientOrder

class ClientOrderAdapter(private val orders: List<ClientOrder>) :
    RecyclerView.Adapter<ClientOrderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtproductTitle);
        val pic: ImageView = itemView.findViewById(R.id.imageView4);
        val feeEachItem: TextView = itemView.findViewById(R.id.txtFeeEachItem);
        val totalEachItem: TextView = itemView.findViewById(R.id.txtTotalEachItem);
        val num: TextView = itemView.findViewById(R.id.txtNumberItem);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_viewholder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.title.text = order.productName
        holder.totalEachItem.setText("$"+ Math.round(order.quantity * order.price));
        holder.num.text = order.quantity.toString()
        holder.feeEachItem.text = order.price.toString()
        //image here.
        //holder.pic.setImageResource(12)
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
