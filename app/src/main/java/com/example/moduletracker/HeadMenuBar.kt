package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityHeadMenuBarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class HeadMenuBar : AppCompatActivity() {
    private lateinit var binding : ActivityHeadMenuBarBinding
    private lateinit var userName: String
    private lateinit var topName: String
    private lateinit var head: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeadMenuBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("username").toString()
        topName = intent.getStringExtra("name").toString()
        head = intent.getStringExtra("head").toString()

        binding.lbName.text = topName
        datePicker()

        binding.btnLarge.isClickable = false
        binding.icX.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        menuBar(binding.btnHeadDashboard, Intent(this, HeadSubjects::class.java),"head")
        menuBar(binding.btnHeadAssigning, Intent(this, AssigningTeachers::class.java),"head2")
        menuBar(binding.btnHeadHelp, Intent(this, HeadHelp::class.java),"head3")
        menuBar(binding.btnHeadAboutUs, Intent(this, HeadAboutUs::class.java),"head4")

        binding.btnHeadLogOut.setOnClickListener {

            val intent = Intent(this, HeadLogOut::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
            finish()
        }

    }
    private fun menuBar(button: Button, intentActivity : Intent, from: String){

        button.setOnClickListener {
            if (head == from){
                finish()
                overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
            }else{
                val intent = intentActivity
                intent.putExtra("username", userName)
                intent.putExtra("name", topName)
                startActivity(intent)
                finishAffinity()
                overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
            }
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat : SimpleDateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
    }
}