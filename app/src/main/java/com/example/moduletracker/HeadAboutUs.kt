package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityHeadAboutUsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class HeadAboutUs : AppCompatActivity() {
    private lateinit var binding: ActivityHeadAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeadAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()

        binding.lbName.text = topName
        datePicker()

        binding.icHeadAuMenuBar.setOnClickListener {

            val intent = Intent(this, HeadMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("head", "head4")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out, R.anim.vanish_in)
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }
}