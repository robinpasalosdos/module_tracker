package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isInvisible
import com.example.moduletracker.databinding.ActivityTeacherHelpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class TeacherHelp : AppCompatActivity() {
    private lateinit var binding : ActivityTeacherHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userNameExtra = intent.getStringExtra("username")
        val topName = intent.getStringExtra("name").toString()
        val userName : String = userNameExtra.toString()
        var index = 0
        binding.lbName.text = topName
        datePicker()

        val pics = listOf<ImageView>(
                binding.icTeacherHelp2,
                binding.icTeacherHelp3,
                binding.icTeacherHelp4,
                binding.icTeacherHelp5,
                binding.icTeacherHelp6,
                binding.icTeacherHelp9
        )

        binding.icTeacherHelpMenuBar.setOnClickListener {
            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("teacher", "teacher3")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnTeacherHelpNext.setOnClickListener {

            if(index in 0..4) {
                index += 1
                val nextDec = 5 - index
                binding.btnTeacherHelpNext.text = "Next (".plus(nextDec.toString()).plus(")")
                pics[index - 1].isInvisible = true
                pics[index - 1].isEnabled = false
                pics[index].isInvisible = false
                pics[index].isEnabled = true

                if(index > 0){

                    binding.btnTeacherHelpBack.isInvisible = false
                    binding.btnTeacherHelpBack.isEnabled = true

                }
                if(index > 4){

                    binding.btnTeacherHelpNext.isInvisible = true
                    binding.btnTeacherHelpNext.isEnabled = false
                }
            }
        }

        binding.btnTeacherHelpBack.setOnClickListener {


            if(index in 1..5) {
                index -= 1
                val nextDec = 5 - index
                binding.btnTeacherHelpNext.text = "Next (".plus(nextDec.toString()).plus(")")
                pics[index + 1].isInvisible = true
                pics[index + 1].isEnabled = false
                pics[index].isInvisible = false
                pics[index].isEnabled = true

                if(index < 5){

                    binding.btnTeacherHelpNext.isInvisible = false
                    binding.btnTeacherHelpNext.isEnabled = true

                }
                if(index < 1){

                    binding.btnTeacherHelpBack.isInvisible = true
                    binding.btnTeacherHelpBack.isEnabled = false
                }
            }
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat : SimpleDateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }
}