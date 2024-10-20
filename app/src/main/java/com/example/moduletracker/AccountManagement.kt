package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityAccountManagementBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AccountManagement : AppCompatActivity() {
    private lateinit var binding : ActivityAccountManagementBinding
    private lateinit var database : DatabaseReference
    private lateinit var userName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()
        val levelList = listOf<String>(
                "7", "8", "9", "10"
        )
        val arrayAdapterLevel = ArrayAdapter(this, R.layout.item2, levelList)
        arrayAdapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spAccLevel.adapter = arrayAdapterLevel

        val sectionList = listOf<String>(
                "1","2","3")
        val arrayAdapterSection = ArrayAdapter(this, R.layout.item2,sectionList)
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spAccSection.adapter = arrayAdapterSection
        binding.edtAccUsername.isEnabled = false

        binding.lbName.text = topName
        datePicker()
        readData()
        updateData()

        binding.icAccStudentMenuBar.setOnClickListener {
            val intent = Intent(this, ActionBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("student", "student2")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

    }
    private fun updateData(){

        binding.btnAccUpdate.setOnClickListener {
            val email : String = binding.edtAccEmail.text.toString()
            val phoneNo : String = binding.edtAccPhoneNo.text.toString()
            val name : String = binding.edtAccName.text.toString()
            val password : String = binding.edtAccPassword.text.toString()
            val level = binding.spAccLevel.selectedItem.toString()
            val section = binding.spAccSection.selectedItem.toString()
            database = FirebaseDatabase.getInstance().getReference("UserStudents")
            val User = UserStudents(email,name,password,phoneNo,level,section,userName)
            if (password.length > 5) {
                database.child(userName).child("Details").setValue(User).addOnSuccessListener {
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

        database = FirebaseDatabase.getInstance().getReference("UserStudents")
        database.child(userName).child("Details").get().addOnSuccessListener {
            val name = it.child("name").value.toString()
            val level = it.child("level").value.toString()
            val section = it.child("section").value.toString()
            val email = it.child("email").value.toString()
            val password = it.child("password").value.toString()
            val phoneNo = it.child("phoneNo").value.toString()
            binding.edtAccName.setText(name)
            binding.edtAccEmail.setText(email)
            binding.edtAccPassword.setText(password)
            binding.edtAccPhoneNo.setText(phoneNo)
            binding.edtAccUsername.setText(userName)
            binding.spAccLevel.setSelection(level.toInt() - 7)
            binding.spAccSection.setSelection(section.toInt() - 1)

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