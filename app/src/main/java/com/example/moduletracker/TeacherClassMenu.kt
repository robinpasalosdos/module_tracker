package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moduletracker.databinding.ActivityTeacherClassMenuBinding
import java.text.SimpleDateFormat
import java.util.*

class TeacherClassMenu : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherClassMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherClassMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()
        val subject = intent.getStringExtra("subject").toString()
        val roomName = intent.getStringExtra("room").toString()

        binding.lbName.text = topName
        datePicker()

        binding.btnTeacherCmClassList.setOnClickListener {

            val intent = Intent(this, TeacherClassList::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("subject", subject)
            intent.putExtra("room", roomName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnTeacherCmModule.setOnClickListener {

            val intent = Intent(this, ModuleMenu::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("subject", subject)
            intent.putExtra("room", roomName)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_exit,R.anim.anim_enter2)
        }

        binding.icTeacherCmMenuBar.setOnClickListener {

            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }
    }

    private fun datePicker() {

        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit2)
    }
}