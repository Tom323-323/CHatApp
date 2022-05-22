package com.lost.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lost.chatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val myRef = database.getReference("message").child("Tom")

        binding.button.setOnClickListener(View.OnClickListener {
            val data = binding.edText.text
            myRef.setValue("$data")

        })

        onResumeData(myRef)

    }

    private fun onResumeData(referenceDB: DatabaseReference){
        referenceDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    textToDisplay.append("\n")
                    textToDisplay.append(snapshot.value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AAA","MainActivity:fun Canceled")
            }

        })
    }
}