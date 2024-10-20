package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityStudentLoginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StudentLogin : AppCompatActivity() {
    private lateinit var binding: ActivityStudentLoginBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStudentLogin.setOnClickListener {
            val userName: String = binding.edtLoginStudentUsername.text.toString()
            val password: String = binding.edtLoginStudentPassword.text.toString()
            if (userName.isNotEmpty()) {

                database = FirebaseDatabase.getInstance().getReference("UserStudents")
                database.child(userName).child("Details").get().addOnSuccessListener {

                    if (it.exists()){

                        val passwordDtb = it.child("password").value.toString()
                        val name = it.child("name").value.toString()
                        if(passwordDtb == password) {

                            Toast.makeText(this, "Successfully login!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Student::class.java)
                            intent.putExtra("username", userName)
                            intent.putExtra("name", name)
                            startActivity(intent)
                            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                            finishAffinity()
                        }else{

                            Toast.makeText(this,"Incorrect password!",Toast.LENGTH_SHORT).show()
                        }
                    }else{

                        Toast.makeText(this,"Invalid username!",Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener{

                    Toast.makeText(this,"Failed!",Toast.LENGTH_SHORT).show()
                }
            }

            else {

                Toast.makeText(this, "Please enter your username!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.icStudentLoginBack.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.anim_down,R.anim.anim_up2)
        }

        binding.lbCreateAccountStudent.setOnClickListener {

            val intent = Intent(this, CreateAccountStudent::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.lbForgotPasswordStudent.setOnClickListener {

            val intent = Intent(this, StudentForgotPassword::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_down,R.anim.anim_up2)
    }
}