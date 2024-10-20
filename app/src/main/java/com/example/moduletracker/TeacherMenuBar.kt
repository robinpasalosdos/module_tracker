package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityTeacherMenuBarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class TeacherMenuBar : AppCompatActivity() {
    private lateinit var binding : ActivityTeacherMenuBarBinding
    private lateinit var userName: String
    private lateinit var topName: String
    private lateinit var teacher: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherMenuBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("username").toString()
        teacher = intent.getStringExtra("teacher").toString()
        topName = intent.getStringExtra("name").toString()

        binding.lbName.text = topName
        datePicker()

        binding.btnLarge.isClickable = false
        binding.icX.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        menuBar(binding.btnTeacherDashboard, Intent(this, Teacher::class.java),"teacher")
        menuBar(binding.btnTeacherAccManagement, Intent(this, AccountManagementTeacher::class.java), "teacher2")
        menuBar(binding.btnTeacherHelp, Intent(this, TeacherHelp::class.java),"teacher3")
        menuBar(binding.btnTeacherAboutUs, Intent(this, TeacherAboutUs::class.java),"teacher4")

        binding.btnTeacherLogOut.setOnClickListener {

            val intent = Intent(this, TeacherLogOut::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
            finish()
        }
    }
    private fun menuBar(button: Button, intentActivity : Intent, from: String){

        button.setOnClickListener {
            if (teacher == from){
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