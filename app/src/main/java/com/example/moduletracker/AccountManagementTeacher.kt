package com.example.moduletracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moduletracker.databinding.ActivityAccountManagementTeacherBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AccountManagementTeacher : AppCompatActivity() {
    private lateinit var binding : ActivityAccountManagementTeacherBinding
    private lateinit var database : DatabaseReference
    private lateinit var userName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagementTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userNameExtra = intent.getStringExtra("username")
        val topName = intent.getStringExtra("name").toString()
        userName = userNameExtra.toString()

        binding.lbName.text = topName
        readData()
        datePicker()

        binding.icAccTeacherMenuBar.setOnClickListener {
            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("teacher", "teacher2")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.edtAccTeacherUsername.isEnabled = false
        binding.btnAccTeacherUpdate.setOnClickListener {
            val email : String = binding.edtAccTeacherEmail.text.toString()
            val phoneNo : String = binding.edtAccTeacherPhoneNo.text.toString()
            val name : String = binding.edtAccTeacherName.text.toString()
            val password : String = binding.edtAccTeacherPassword.text.toString()
            database = FirebaseDatabase.getInstance().getReference("UserTeachers")
            val user = UserStudents(email,name,password,phoneNo,userName)
            if (password.length > 5) {
                database.child(userName).child("Details").setValue(user).addOnSuccessListener {
                    Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {

                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }

            }else{

                Toast.makeText(this, "Password must contains more than 6 characters", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readData() {

        database = FirebaseDatabase.getInstance().getReference("UserTeachers")
        database.child(userName).child("Details").get().addOnSuccessListener {
            val name = it.child("name").value.toString()
            val email = it.child("email").value.toString()
            val password = it.child("password").value.toString()
            val phoneNo = it.child("phoneNo").value.toString()
            binding.edtAccTeacherName.setText(name)
            binding.edtAccTeacherEmail.setText(email)
            binding.edtAccTeacherPassword.setText(password)
            binding.edtAccTeacherPhoneNo.setText(phoneNo)
            binding.edtAccTeacherUsername.setText(userName)

        }.addOnFailureListener{

            Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat : SimpleDateFormat = SimpleDateFormat("E dd/MM/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }
}