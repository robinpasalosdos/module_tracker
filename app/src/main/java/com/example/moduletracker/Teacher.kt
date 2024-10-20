package com.example.moduletracker

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isInvisible
import com.example.moduletracker.databinding.ActivityTeacherBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Teacher : AppCompatActivity() {

    private lateinit var binding : ActivityTeacherBinding
    private lateinit var database : DatabaseReference
    private lateinit var userName : String
    private lateinit var topName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("username").toString()
        topName = intent.getStringExtra("name").toString()

        val subjectList = listOf<String>(
                "English", "Math", "Science", "AP", "ESP", "Filipino", "TLE", "MAPEH"
        )
        binding.lbName.text = topName
        datePicker()

        if (userName == "Robin09" || userName == "Ronald" || userName == "Charles"){

            binding.btnHead.isInvisible = false
            binding.btnHead.isEnabled = true
        }

        binding.icTeacherMenuBar.setOnClickListener {

            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("teacher", "teacher")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnHead.setOnClickListener {

            val intent = Intent(this, HeadSubjects::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        database = FirebaseDatabase.getInstance().getReference("UserTeachers")

        var index = 0
        for (sub in subjectList){

            database = FirebaseDatabase.getInstance().getReference("UserTeachers")
            database.child(userName).child("Subject").child(sub).get().addOnSuccessListener {

                if(it.exists()) {
                    addButton(sub,index)
                    index += 1
                }

            }.addOnFailureListener {

                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
            }
        }

        database.child(userName).child("Subject").get().addOnSuccessListener {

            if (!it.exists()){

                binding.tvSubject.text = "No subject"
                binding.tvContactAdmin.text = "Contact your admin to assign your classes and subjects."
            }
        }
    }

    private fun addButton(subject: String, index: Int) {

        val button = Button(this)
        val space = Space(this)
        button.height = 150
        button.text = subject
        button.setTextColor(Color.WHITE)
        button.textSize = 18F
        button.isAllCaps = false
        button.background = button.context.resources.getDrawable(R.drawable.mt_button3)
        space.minimumHeight = 25
        if(index%2 == 0) {
            binding.llTeacherLayout.addView(button)
            binding.llTeacherLayout.addView(space)
        }else{
            binding.llTeacherLayout2.addView(button)
            binding.llTeacherLayout2.addView(space)
        }

        button.setOnClickListener {

            val intent = Intent(this, Teacher2::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("subject", subject)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_exit,R.anim.anim_enter2)

        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }
}