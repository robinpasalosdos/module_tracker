package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isInvisible
import com.example.moduletracker.databinding.ActivityHelpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Help : AppCompatActivity() {
    private lateinit var binding : ActivityHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()
        var index = 0
        binding.lbName.text = topName
        datePicker()
        val pics = listOf<ImageView>(
            binding.icStudentHelp1,
            binding.icStudentHelp7,
            binding.icStudentHelp8
        )
        binding.icStudentHelpMenuBar.setOnClickListener {

            val intent = Intent(this, ActionBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("student", "student3")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnStudentHelpNext.setOnClickListener {

            if(index in 0..1) {
                index += 1
                val nextDec = 2 - index
                binding.btnStudentHelpNext.text = "Next (".plus(nextDec.toString()).plus(")")
                pics[index - 1].isInvisible = true
                pics[index - 1].isEnabled = false
                pics[index].isInvisible = false
                pics[index].isEnabled = true

                if(index > 0){

                    binding.btnStudentHelpBack.isInvisible = false
                    binding.btnStudentHelpBack.isEnabled = true

                }
                if(index > 1){

                    binding.btnStudentHelpNext.isInvisible = true
                    binding.btnStudentHelpNext.isEnabled = false
                }
            }
        }

        binding.btnStudentHelpBack.setOnClickListener {


            if(index in 1..2) {
                index -= 1
                val nextDec = 2 - index
                binding.btnStudentHelpNext.text = "Next (".plus(nextDec.toString()).plus(")")
                pics[index + 1].isInvisible = true
                pics[index + 1].isEnabled = false
                pics[index].isInvisible = false
                pics[index].isEnabled = true

                if(index < 2){

                    binding.btnStudentHelpNext.isInvisible = false
                    binding.btnStudentHelpNext.isEnabled = true

                }
                if(index < 1){

                    binding.btnStudentHelpBack.isInvisible = true
                    binding.btnStudentHelpBack.isEnabled = false
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