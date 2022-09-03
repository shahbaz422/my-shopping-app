package com.example.myshoppingapp.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myshoppingapp.R
import com.example.myshoppingapp.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    private lateinit var preferences:SharedPreferences
    private lateinit var totalCost :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences=this.getSharedPreferences("user", MODE_PRIVATE)

        totalCost=intent.getStringExtra("totalCost")!!

        loadUserInfo()

        binding.proceed.setOnClickListener {
            validateData(
                binding.userName.text.toString(),
                binding.userNumber.text.toString(),
                binding.userArea.text.toString(),
                binding.userCity.text.toString(),
                binding.userPinCode.text.toString(),
                binding.userState.text.toString()
            )
        }

    }

    private fun validateData(
        name: String,
        number: String,
        area: String,
        city: String,
        pincode: String,
        state: String
    ) {
        if(name.isEmpty()|| number.isEmpty()|| area.isEmpty()|| city.isEmpty()|| pincode.isEmpty()|| state.isEmpty()){
            Toast.makeText(this,"all fields are mandatory",Toast.LENGTH_SHORT).show()
        }else{
            storeData(area,city,pincode,state)
        }
    }

    private fun storeData(area: String, city: String, pincode: String, state: String) {
        val map= hashMapOf<String,Any>()
        map["Area"]=area
        map["city"]=city
        map["pincode"]=pincode
        map["state"]=state

        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .update(map).addOnSuccessListener {

                val intent= Intent(this, CheckoutActivity::class.java)
                val b=Bundle()
                b.putStringArrayList("productIds",intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost",totalCost)
                intent.putExtras(b)

                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show()
            }
    }


    private fun loadUserInfo() {

        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userPhoneNumber"))
                binding.userArea.setText(it.getString("Area"))
                binding.userCity.setText(it.getString("city"))
                binding.userPinCode.setText(it.getString("pincode"))
                binding.userState.setText(it.getString("state"))
            }
    }
}