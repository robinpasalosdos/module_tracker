package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityStudentAboutUsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class StudentAboutUs : AppCompatActivity() {

    private lateinit var binding : ActivityStudentAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()

        binding.lbName.text = topName
        datePicker()

        binding.icStudentAboutUsMenuBar.setOnClickListener {
            val intent = Intent(this, ActionBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("student", "student4")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat : SimpleDateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }
}