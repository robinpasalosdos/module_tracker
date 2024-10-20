package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.moduletracker.databinding.ActivityActionBarBinding
import java.text.SimpleDateFormat
import java.util.*

class ActionBar : AppCompatActivity() {
    private lateinit var binding : ActivityActionBarBinding
    private lateinit var userName: String
    private lateinit var topName: String
    private lateinit var student: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActionBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("username").toString()
        topName = intent.getStringExtra("name").toString()
        student = intent.getStringExtra("student").toString()

        binding.lbName.text = topName
        datePicker()

        binding.btnLarge.isClickable = false
        binding.icX.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        menuBar(binding.btnAccManagement, Intent(this, AccountManagement::class.java),"student2")
        menuBar(binding.btnDashboard, Intent(this, Student::class.java),"student")
        menuBar(binding.btnAboutUs, Intent(this, StudentAboutUs::class.java),"student4")
        menuBar(binding.btnHelp, Intent(this, Help::class.java),"student3")

        binding.btnLogOut.setOnClickListener {
            val intent = Intent(this, LogOut::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
            finish()
        }
    }

    private fun menuBar(button: Button, intentActivity : Intent , from: String){

        button.setOnClickListener {
            if (student == from){
                finish()
                overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
            }else{
                val intent = intentActivity
                intent.putExtra("username", userName)
                intent.putExtra("name", topName)
                startActivity(intent)
                overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                finishAffinity()
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