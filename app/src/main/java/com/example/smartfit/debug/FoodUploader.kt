package com.example.smartfit.debug

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object FoodUploader {

    fun uploadFoods() {

        val db = FirebaseFirestore.getInstance()

        val foods = listOf(
            mapOf("name" to "Chicken Breast","calories" to 165,"protein" to 31,"carbs" to 0,"fat" to 4,"category" to "protein"),
            mapOf("name" to "Grilled Chicken","calories" to 200,"protein" to 32,"carbs" to 0,"fat" to 6,"category" to "protein"),
            mapOf("name" to "Boiled Egg","calories" to 78,"protein" to 6,"carbs" to 1,"fat" to 5,"category" to "protein"),
            mapOf("name" to "Egg White","calories" to 17,"protein" to 4,"carbs" to 0,"fat" to 0,"category" to "protein"),
            mapOf("name" to "Paneer","calories" to 265,"protein" to 18,"carbs" to 6,"fat" to 21,"category" to "protein"),
            mapOf("name" to "Low Fat Paneer","calories" to 180,"protein" to 20,"carbs" to 5,"fat" to 9,"category" to "protein"),
            mapOf("name" to "Tofu","calories" to 144,"protein" to 15,"carbs" to 4,"fat" to 9,"category" to "protein"),
            mapOf("name" to "Fish Curry","calories" to 210,"protein" to 22,"carbs" to 5,"fat" to 12,"category" to "protein"),
            mapOf("name" to "Grilled Fish","calories" to 190,"protein" to 28,"carbs" to 0,"fat" to 9,"category" to "protein"),
            mapOf("name" to "Prawns","calories" to 99,"protein" to 24,"carbs" to 0,"fat" to 1,"category" to "protein"),

            mapOf("name" to "Milk","calories" to 120,"protein" to 8,"carbs" to 12,"fat" to 5,"category" to "dairy"),
            mapOf("name" to "Curd","calories" to 98,"protein" to 5,"carbs" to 7,"fat" to 4,"category" to "dairy"),
            mapOf("name" to "Greek Yogurt","calories" to 130,"protein" to 10,"carbs" to 4,"fat" to 7,"category" to "dairy"),
            mapOf("name" to "Butter","calories" to 102,"protein" to 0,"carbs" to 0,"fat" to 12,"category" to "dairy"),
            mapOf("name" to "Cheese Slice","calories" to 113,"protein" to 7,"carbs" to 1,"fat" to 9,"category" to "dairy"),

            mapOf("name" to "White Rice","calories" to 205,"protein" to 4,"carbs" to 45,"fat" to 0,"category" to "carb"),
            mapOf("name" to "Brown Rice","calories" to 216,"protein" to 5,"carbs" to 44,"fat" to 2,"category" to "carb"),
            mapOf("name" to "Fried Rice","calories" to 330,"protein" to 7,"carbs" to 45,"fat" to 14,"category" to "carb"),
            mapOf("name" to "Roti","calories" to 120,"protein" to 3,"carbs" to 18,"fat" to 3,"category" to "carb"),
            mapOf("name" to "Paratha","calories" to 260,"protein" to 5,"carbs" to 30,"fat" to 14,"category" to "carb"),
            mapOf("name" to "Naan","calories" to 310,"protein" to 9,"carbs" to 52,"fat" to 6,"category" to "carb"),
            mapOf("name" to "Bread","calories" to 80,"protein" to 3,"carbs" to 15,"fat" to 1,"category" to "carb"),
            mapOf("name" to "Oats","calories" to 150,"protein" to 5,"carbs" to 27,"fat" to 3,"category" to "carb"),

            mapOf("name" to "Dal Tadka","calories" to 180,"protein" to 9,"carbs" to 22,"fat" to 6,"category" to "indian"),
            mapOf("name" to "Rajma","calories" to 220,"protein" to 11,"carbs" to 33,"fat" to 4,"category" to "indian"),
            mapOf("name" to "Chole","calories" to 240,"protein" to 12,"carbs" to 35,"fat" to 6,"category" to "indian"),
            mapOf("name" to "Khichdi","calories" to 210,"protein" to 8,"carbs" to 30,"fat" to 5,"category" to "indian"),
            mapOf("name" to "Idli","calories" to 58,"protein" to 2,"carbs" to 12,"fat" to 0,"category" to "indian"),
            mapOf("name" to "Dosa","calories" to 168,"protein" to 4,"carbs" to 27,"fat" to 4,"category" to "indian"),
            mapOf("name" to "Masala Dosa","calories" to 300,"protein" to 6,"carbs" to 45,"fat" to 9,"category" to "indian"),
            mapOf("name" to "Biryani","calories" to 450,"protein" to 20,"carbs" to 55,"fat" to 18,"category" to "indian"),

            mapOf("name" to "Banana","calories" to 105,"protein" to 1,"carbs" to 27,"fat" to 0,"category" to "fruit"),
            mapOf("name" to "Apple","calories" to 95,"protein" to 0,"carbs" to 25,"fat" to 0,"category" to "fruit"),
            mapOf("name" to "Orange","calories" to 62,"protein" to 1,"carbs" to 15,"fat" to 0,"category" to "fruit"),
            mapOf("name" to "Mango","calories" to 135,"protein" to 1,"carbs" to 35,"fat" to 1,"category" to "fruit"),

            mapOf("name" to "Peanuts","calories" to 170,"protein" to 7,"carbs" to 6,"fat" to 14,"category" to "snack"),
            mapOf("name" to "Almonds","calories" to 164,"protein" to 6,"carbs" to 6,"fat" to 14,"category" to "snack"),
            mapOf("name" to "Cashews","calories" to 155,"protein" to 5,"carbs" to 9,"fat" to 12,"category" to "snack"),
            mapOf("name" to "Protein Bar","calories" to 220,"protein" to 20,"carbs" to 23,"fat" to 7,"category" to "snack"),

            mapOf("name" to "Tea","calories" to 40,"protein" to 1,"carbs" to 6,"fat" to 1,"category" to "drink"),
            mapOf("name" to "Coffee","calories" to 2,"protein" to 0,"carbs" to 0,"fat" to 0,"category" to "drink"),
            mapOf("name" to "Orange Juice","calories" to 110,"protein" to 2,"carbs" to 26,"fat" to 0,"category" to "drink"),

            mapOf("name" to "Gulab Jamun","calories" to 150,"protein" to 2,"carbs" to 25,"fat" to 5,"category" to "sweet"),
            mapOf("name" to "Ice Cream","calories" to 210,"protein" to 4,"carbs" to 24,"fat" to 11,"category" to "sweet"),
            mapOf("name" to "Chocolate Cake","calories" to 350,"protein" to 5,"carbs" to 50,"fat" to 15,"category" to "sweet")
        )

        foods.forEach { food ->
            db.collection("foods")
                .add(food)
                .addOnSuccessListener {
                    Log.d("FOOD_UPLOAD", "Added: ${food["name"]}")
                }
                .addOnFailureListener {
                    Log.e("FOOD_UPLOAD", "Failed: ${food["name"]}")
                }
        }
    }
}
