package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isInvisible
import com.example.moduletracker.databinding.ActivityHeadHelpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class HeadHelp : AppCompatActivity() {
    private lateinit var binding : ActivityHeadHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeadHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userNameExtra = intent.getStringExtra("username")
        val topName = intent.getStringExtra("name").toString()
        val userName : String = userNameExtra.toString()
        var index = 0
        binding.lbName.text = topName
        datePicker()

        val pics = listOf<ImageView>(
                binding.icHeadHelp2,
                binding.icHeadHelp3,
                binding.icHeadHelp4,
                binding.icHeadHelp5,
                binding.icHeadHelp6,
                binding.icHeadHelp9
        )

        binding.icHeadHelpMenuBar.setOnClickListener {
            val intent = Intent(this, HeadMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("head3", "head3")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out, R.anim.vanish_in)
        }

        binding.btnHeadHelpNext.setOnClickListener {

            if(index in 0..4) {
                index += 1
                val nextDec = 5 - index
                binding.btnHeadHelpNext.text = "Next (".plus(nextDec.toString()).plus(")")
                pics[index - 1].isInvisible = true
                pics[index - 1].isEnabled = false
                pics[index].isInvisible = false
                pics[index].isEnabled = true

                if(index > 0){

                    binding.btnHeadHelpBack.isInvisible = false
                    binding.btnHeadHelpBack.isEnabled = true

                }
                if(index > 4){

                    binding.btnHeadHelpNext.isInvisible = true
                    binding.btnHeadHelpNext.isEnabled = false
                }
            }
        }

        binding.btnHeadHelpBack.setOnClickListener {


            if(index in 1..5) {
                index -= 1
                val nextDec = 5 - index
                binding.btnHeadHelpNext.text = "Next (".plus(nextDec.toString()).plus(")")
                pics[index + 1].isInvisible = true
                pics[index + 1].isEnabled = false
                pics[index].isInvisible = false
                pics[index].isEnabled = true

                if(index < 5){

                    binding.btnHeadHelpNext.isInvisible = false
                    binding.btnHeadHelpNext.isEnabled = true

                }
                if(index < 1){

                    binding.btnHeadHelpBack.isInvisible = true
                    binding.btnHeadHelpBack.isEnabled = false
                }
            }
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }
}