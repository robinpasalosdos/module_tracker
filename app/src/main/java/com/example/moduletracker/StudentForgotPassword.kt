package com.example.moduletracker

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.moduletracker.databinding.ActivityStudentForgotPasswordBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class StudentForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityStudentForgotPasswordBinding
    private lateinit var database: DatabaseReference
    private lateinit var userName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS), 111)
        }

        binding.icStudentFpBackA.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        sendSms()
    }

    private fun sendSms() {

        binding.btnStudentFpRp.setOnClickListener {

            database = FirebaseDatabase.getInstance().getReference("UserStudents")
            userName = binding.edtStudentFpUsername.text.toString()
            if (userName.isNotEmpty()) {

                database.child(userName).child("Details").get().addOnSuccessListener {
                    if (it.exists()) {
                        val phoneNo = it.child("phoneNo").value.toString()
                        val source: List<Char> = ('A'..'Z') + ('0'..'9')
                        val code = List(6) { source.random() }.joinToString("")
                        SmsManager.getDefault().sendTextMessage(phoneNo, "Module Tracker", "Your verification code is =>".plus(code), null, null)
                        Toast.makeText(this, "SMS sent!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, StudentForgotPasswordVerification::class.java)
                        intent.putExtra("code", code)
                        intent.putExtra("username", userName)
                        startActivity(intent)
                        overridePendingTransition(R.anim.anim_exit,R.anim.anim_enter2)
                    }else{

                        Toast.makeText(this,"Invalid username!",Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener{

                    Toast.makeText(this,"Failed!",Toast.LENGTH_SHORT).show()
                }

            }else{

                Toast.makeText(this, "Please enter your username!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
    }
}