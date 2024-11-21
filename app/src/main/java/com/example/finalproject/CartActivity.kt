package com.example.finalproject

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()

    private val userId: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }
    private val databaseRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("carts").child(userId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartRecyclerView = findViewById(R.id.recyclerView)
        totalPriceTextView = findViewById(R.id.totalTxt)

        cartAdapter = CartAdapter(cartItems) { updateTotalPrice() }

        cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }

        initializeEmptyCartNode()
        loadCartItems()
    }

    private fun initializeEmptyCartNode() {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    databaseRef.setValue(emptyMap<String, Any>()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("CartActivity", "Empty cart node initialized.")
                        } else {
                            Log.e("CartActivity", "Failed to initialize cart node.")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CartActivity", "Error checking cart node: ${error.message}")
            }
        })
    }

    private fun loadCartItems() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItems.clear()

                for (itemSnapshot in snapshot.children) {
                    val cartItem = itemSnapshot.getValue(CartItem::class.java)
                    cartItem?.let {
                        cartItems.add(it)
                    }
                }

                cartAdapter.notifyDataSetChanged()

                updateTotalPrice()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CartActivity", "Error loading cart: ${error.message}")
            }
        })
    }

    private fun updateTotalPrice() {
        val totalPrice = cartItems.sumOf { it.price * it.quantity }
        totalPriceTextView.text = String.format("$%.2f", totalPrice)
    }
}
