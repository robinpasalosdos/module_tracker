package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityHeadSubjectsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class HeadSubjects : AppCompatActivity() {

    private lateinit var binding: ActivityHeadSubjectsBinding
    private lateinit var userName: String
    private lateinit var topName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeadSubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("username").toString()
        topName = intent.getStringExtra("name").toString()
        binding.lbName.text = topName
        datePicker()

        binding.icHeadMenuBar.setOnClickListener {

            val intent = Intent(this, HeadMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("head", "head")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        funButton(binding.btnHeadEnglish,"English")
        funButton(binding.btnHeadMath,"Math")
        funButton(binding.btnHeadScience,"Science")
        funButton(binding.btnHeadAp,"AP")
        funButton(binding.btnHeadEsp,"ESP")
        funButton(binding.btnHeadFilipino,"Filipino")
        funButton(binding.btnHeadTle,"TLE")
        funButton(binding.btnHeadMapeh,"MAPEH")
    }

    private fun funButton(button : Button, subject: String){

        button.setOnClickListener {

            val intent = Intent(this, AssignedTeacherLists::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("subject", subject)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
    }
}