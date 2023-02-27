package com.example.ehcf.RatingAndReviews

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.R
import com.example.ehcf.databinding.ActivityRatingBinding


class Rating : AppCompatActivity() {
    private lateinit var binding: ActivityRatingBinding
    lateinit var ratingBar: RatingBar
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvAddPhoto.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            val selectImage = 1234
            startActivityForResult(i, selectImage)
        }

        ratingBar = findViewById(R.id.ratingBar)
        ratingBar.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        ratingBar.numStars = 5
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                Toast.makeText(
                    this@Rating, "Stars: " +
                            rating.toInt(), Toast.LENGTH_SHORT
                ).show()
            }

    }
//
//        val rBar = RatingBar(this)
//        val layoutParams = LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT)
//        rBar.layoutParams = layoutParams
//        rBar.stepSize = 1f
//        rBar.numStars = 5
////
////        //create button
////        val button = Button(this)
////        val layoutParams1 = LinearLayout.LayoutParams(
////            ViewGroup.LayoutParams.MATCH_PARENT,
////            ViewGroup.LayoutParams.WRAP_CONTENT)
////        button.text="Submit Rating"
//
//
//        val linearLayout = findViewById<LinearLayout>(R.id.container)
//        // Add RatingBar and button to LinearLayout
//        linearLayout?.addView(rBar)
     //   linearLayout?.addView(button)

//        button.setOnClickListener {
//            val msg = rBar.rating.toString()
//            Toast.makeText(this@Rating, "Given Rating: "+msg,
//                Toast.LENGTH_SHORT).show()
//        }





}