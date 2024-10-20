package com.example.moduletracker

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityTeacherAccountManagementStudentBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class TeacherAccountManagementStudent : AppCompatActivity() {
    private lateinit var binding : ActivityTeacherAccountManagementStudentBinding
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherAccountManagementStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()
        val studentUserName = intent.getStringExtra("studentUsername").toString()

        binding.edtAccUsername.isEnabled = false
        binding.lbName.text = topName
        datePicker()
        readData(studentUserName)

        binding.icAccStudentMenuBar.setOnClickListener {
            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
        }
    }

    private fun readData(studentUserName: String) {

        database = FirebaseDatabase.getInstance().getReference("UserStudents")
        database.child(studentUserName).child("Details").get().addOnSuccessListener {
            val name = it.child("name").value.toString()
            val level = it.child("level").value.toString()
            val section = it.child("section").value.toString()
            val email = it.child("email").value.toString()
            val phoneNo = it.child("phoneNo").value.toString()
            binding.textView2.text = name
            binding.edtAccEmail.setText(email)
            binding.edtAccUsername.setText(studentUserName)
            binding.edtAccLevel.setText(level)
            binding.edtAccSection.setText(section)
            binding.edtAccPhoneNo.setText(phoneNo)

        }.addOnFailureListener{

            Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E dd/MM/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }
}