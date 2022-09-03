package com.example.myshoppingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshoppingapp.databinding.LayoutProductItemBinding
import com.example.myshoppingapp.model.AddProductModel
import com.example.myshoppingapp.ui.activity.ProductDetailsActivity

class ProductAdapter(val context: Context, val list:ArrayList<AddProductModel>)
    :RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    inner class ProductViewHolder(val binding: LayoutProductItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding=LayoutProductItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data=list[position]
        Glide.with(context).load(data.productCoverImg).into(holder.binding.imageView2)
        holder.binding.textView1.text=data.productName
        holder.binding.textView2.text=data.productCategory
       // holder.binding.textView3.text=data.productMrp
        holder.binding.textView3.setText("MRP: ₹${data.productMrp}")

        //holder.binding.button1.text=data.productSp
        holder.binding.button1.setText("get at ₹${data.productSp}")

        holder.itemView.setOnClickListener {
            val intent= Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
        holder.binding.button2.setOnClickListener {
            val intent= Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}