package com.example.myshoppingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshoppingapp.databinding.LayoutCartItemBinding
import com.example.myshoppingapp.roomdb.AppDatabase
import com.example.myshoppingapp.roomdb.ProductModel
import com.example.myshoppingapp.ui.activity.ProductDetailsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context: Context,val list:List<ProductModel>):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    inner class CartViewHolder(val binding: LayoutCartItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding=LayoutCartItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView3)
        holder.binding.textView9.text=list[position].productName
        holder.binding.textView10.text=list[position].productSp


        holder.itemView.setOnClickListener{
            val intent=Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)

        }

        val dao=AppDatabase.getInstance(context).productDao()
        holder.binding.imageView5.setOnClickListener {
            Toast.makeText(context,"item removed from cart",Toast.LENGTH_SHORT).show()
            GlobalScope.launch {
                Dispatchers.IO
                dao.deleteProduct(
                    ProductModel(
                        list[position].productId,
                        list[position].productName,
                        list[position].productImage,
                        list[position].productSp
                    )
                )
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

}