package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityStudentForgotPasswordChangeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StudentForgotPasswordChange : AppCompatActivity() {
    private lateinit var binding: ActivityStudentForgotPasswordChangeBinding
    private lateinit var database: DatabaseReference
    private lateinit var userName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentForgotPasswordChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userName = intent.getStringExtra("username").toString()

        binding.icStudentFpcBackA.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit2)
        }

        updatePassword()
    }

    private fun updatePassword() {
        binding.btnStudentFpcChange.setOnClickListener {
            val newPassword = binding.edtStudentFpcChangePassword.text.toString()
            if(newPassword.isNotEmpty()) {
                database = FirebaseDatabase.getInstance().getReference("UserStudents")
                val user = mapOf<String, String>("password" to newPassword)
                database.child(userName).child("Details").updateChildren(user).addOnSuccessListener {

                    val intent = Intent(this, StudentLogin::class.java)
                    Toast.makeText(this, "Successfully Changed", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                    finishAffinity()

                }.addOnFailureListener {

                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit2)
    }
}
