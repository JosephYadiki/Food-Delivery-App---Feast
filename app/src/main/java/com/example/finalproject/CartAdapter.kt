package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onQuantityChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.picCard)
        val itemTitle: TextView = view.findViewById(R.id.title2Txt)
        val itemPrice: TextView = view.findViewById(R.id.feeEachItem)
        val itemTotal: TextView = view.findViewById(R.id.totalEachItem)
        val itemQuantity: TextView = view.findViewById(R.id.numberItemTxt)
        val plusButton: ImageView = view.findViewById(R.id.plusCardBtn)
        val minusButton: ImageView = view.findViewById(R.id.minusCardBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_card, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.itemTitle.text = cartItem.title
        holder.itemPrice.text = String.format("$%.2f", cartItem.price)
        holder.itemQuantity.text = cartItem.quantity.toString()
        holder.itemTotal.text = String.format("$%.2f", cartItem.price * cartItem.quantity)

        Glide.with(holder.itemView.context)
            .load(cartItem.imageResource)
            .into(holder.itemImage)

        holder.plusButton.setOnClickListener {
            cartItem.quantity += 1
            notifyItemChanged(position)
            updateCartInFirebase(cartItem)
        }

        holder.minusButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity -= 1
                notifyItemChanged(position)
                updateCartInFirebase(cartItem)
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size

    private fun updateCartInFirebase(cartItem: CartItem) {
        val databaseRef = FirebaseDatabase.getInstance()
            .getReference("carts")
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .child(cartItem.id)
        databaseRef.setValue(cartItem).addOnCompleteListener {
            onQuantityChanged()
        }
    }
}
