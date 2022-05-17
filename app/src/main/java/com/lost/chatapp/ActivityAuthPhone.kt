package com.lost.chatapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.lost.chatapp.databinding.ActivityAuthPhoneBinding

class ActivityAuthPhone: AppCompatActivity() {

    lateinit var binding: ActivityAuthPhoneBinding
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationID:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun OnCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityAuthPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }


}