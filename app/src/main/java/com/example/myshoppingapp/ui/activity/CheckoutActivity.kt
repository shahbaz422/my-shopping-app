package com.example.myshoppingapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myshoppingapp.R
import com.example.myshoppingapp.roomdb.AppDatabase
import com.example.myshoppingapp.roomdb.ProductModel
import com.example.myshoppingapp.ui.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class CheckoutActivity : AppCompatActivity(),PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)


        val checkout=Checkout()
        checkout.setKeyID("rzp_test_2UZUAcF6T5u0DV");
        //keysecret= v7nEfq9dygqUPn1kNzKeuRej

        val price=intent.getStringExtra("totalCost")

        try {
            val options = JSONObject()
            options.put("name", "My Shopping App")
            options.put("description", "Best Ecommerce Application")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#267050")
            options.put("currency", "INR")
            options.put("amount", (price!!.toInt()*100)) //pass amount in currency subunits
            options.put("prefill.email", "mohammedshahbaz422@gmail.com")
            options.put("prefill.contact", "7050085361")
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"payment successful",Toast.LENGTH_SHORT).show()
        uploadData()
        startActivity(Intent(this,MainActivity::class.java))
    }

    private fun uploadData() {
        val id=intent.getStringArrayListExtra("productIds")
        for(currentId in id!!){
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {

        val dao=AppDatabase.getInstance(this).productDao()


        Firebase.firestore.collection("products")
            .document(productId!!).get().addOnSuccessListener {

                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteProduct(ProductModel(productId))
                }

                saveData(it.getString("productName"),
                    it.getString("productSp"),
                    it.getString("productCoverImg"),
                    productId)
            }
    }

    private fun saveData(name: String?, price: String?, image: String?, productId: String) {
        val data= hashMapOf<String,Any>()
        val preferences=this.getSharedPreferences("user", MODE_PRIVATE)
        data["name"]=name!!
        data["price"]=price!!
        data["image"]=image!!
        data["productId"]=productId
        data["status"]="Ordered"
        data["userId"]=preferences.getString("number","")!!

        val firestore=Firebase.firestore.collection("allOrders")
        val key=firestore.document().id
        data["orderId"]= key

        firestore.add(data).addOnSuccessListener {
            Toast.makeText(this,"order placed",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }.addOnFailureListener {
            startActivity(Intent(this,MainActivity::class.java))
            Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show()
        }



    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"payment failed",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,MainActivity::class.java))
    }
}





















