package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FoodAdapter(private val foodList: ArrayList<FoodItem>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.pic)
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val priceTextView: TextView = itemView.findViewById(R.id.fee)
        val addButton: TextView = itemView.findViewById(R.id.addBtn)

        init {
            addButton.setOnClickListener {
                val foodItem = foodList[adapterPosition]
                Toast.makeText(itemView.context, "${foodItem.title} added to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_popular, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentFoodItem = foodList[position]

        Glide.with(holder.itemView.context)
            .load(currentFoodItem.imageResource)
            .into(holder.imageView)

        holder.titleTextView.text = currentFoodItem.title
        holder.priceTextView.text = "$${currentFoodItem.price}"
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}
