package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter
    private lateinit var foodList: ArrayList<FoodItem>
    private lateinit var locationButton : ImageButton
    private lateinit var profileButton : ImageView
    private lateinit var cartButton : FloatingActionButton
    private lateinit var categoryRecyclerView: RecyclerView
    private val categories = mutableListOf<FoodCategory>()
    private lateinit var categoryAdapter: CategoryAdapter

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val foodRef: DatabaseReference = database.getReference("foods")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        locationButton = findViewById(R.id.locationButton)
        profileButton = findViewById(R.id.profileButton)
        cartButton = findViewById(R.id.cartButton)
        cartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        locationButton.setOnClickListener {
            startActivity(Intent(this, LocationActivity::class.java))
        }
        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView = findViewById(R.id.recyclerView)
        categoryAdapter = CategoryAdapter(categories) { category ->
            showFoodsByCategory(category)
        }

        categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        loadCategoriesFromFirebase()
        foodList = ArrayList()

        fetchFoodData()
    }
    private fun showFoodsByCategory(category: FoodCategory) {
        Log.d("HomeActivity", "Clicked category: ${category.name}")
    }


    private fun fetchFoodData() {
        foodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodList.clear()

                for (foodSnapshot in snapshot.children) {
                    val foodItem = foodSnapshot.getValue(FoodItem::class.java)
                    foodItem?.let {
                        foodList.add(it)
                    }
                }

                adapter = FoodAdapter(foodList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Failed to load food data.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun loadCategoriesFromFirebase() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("categories")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categories.clear()
                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(FoodCategory::class.java)
                    category?.let { categories.add(it) }
                }
                categoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeActivity", "Error loading categories: ${error.message}")
            }
        })
    }

}
