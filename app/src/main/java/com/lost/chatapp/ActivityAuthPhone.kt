package com.lost.chatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.lost.chatapp.databinding.ActivityAuthPhoneBinding
import java.util.concurrent.TimeUnit

class ActivityAuthPhone: AppCompatActivity() {

    lateinit var binding: ActivityAuthPhoneBinding
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationID:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        // Chek the current user
        val currentUser = auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }

        binding.btnSend.setOnClickListener(View.OnClickListener {
            login()
        })


    }

    private fun login() {
        var phoneNumber = binding.etPhoneNumber.text.toString().trim()
        if(phoneNumber.isNotEmpty()){
            sendVerificationCode(phoneNumber)
        } else {
            Toast.makeText(applicationContext, "YOu are idiot!!! Enter mobile number!", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(20L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


}