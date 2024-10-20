package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moduletracker.databinding.ActivityHeadLogOutBinding
import java.text.SimpleDateFormat
import java.util.*

class HeadLogOut : AppCompatActivity() {

    private lateinit var binding : ActivityHeadLogOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeadLogOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()

        binding.lbName.text = topName
        datePicker()

        binding.btnHeadLogoutCancel.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnHeadLogoutOk.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
            finishAffinity()
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