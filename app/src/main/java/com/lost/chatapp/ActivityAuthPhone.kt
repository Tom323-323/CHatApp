package com.lost.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.lost.chatapp.databinding.ActivityAuthPhoneBinding
import com.lost.chatapp.exten.makeToast
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

        auth = FirebaseAuth.getInstance() //Init

        // Chek the current user
        val currentUser = auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }

        binding.btnSend.setOnClickListener(View.OnClickListener {
            login()
        })

        //Callback function
        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                startActivity(Intent(applicationContext,MainActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                makeToast("Failed verification!")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken)
            {
                Log.e("AAA", verificationId)
                storedVerificationID = verificationId
                resendToken = token
            }
        }
        // Verification__________________________________________

        binding.btnOk.setOnClickListener(View.OnClickListener {
            val otpCode = binding.etCode.text.toString().trim()
            if(otpCode.isNotEmpty()){
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationID, otpCode)
                signInWithPhoneAuthCredential(credential)
            } else{
                makeToast("Idiot! Enter CODE!")
            }
        })
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                //If code is done
                startActivity(Intent(applicationContext,MainActivity::class.java))
                finish()
            } else{
                // If code is wrong
                if(task.exception is FirebaseAuthInvalidCredentialsException){
                    makeToast("Wrong code!!!")
                }
            }
        }
    }

    // Send phone number
    private fun login() {
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        if(phoneNumber.isNotEmpty()){
            sendVerificationCode("+7$phoneNumber")
        } else {
            makeToast("You are idiot!!! Enter mobile number!")
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