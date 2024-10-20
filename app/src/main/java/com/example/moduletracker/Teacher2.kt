package com.example.moduletracker

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Space
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityTeacher2Binding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class Teacher2 : AppCompatActivity() {
    private lateinit var binding : ActivityTeacher2Binding
    private lateinit var database : DatabaseReference
    private lateinit var userName : String
    private lateinit var topName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacher2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        userName = intent.getStringExtra("username").toString()
        topName = intent.getStringExtra("name").toString()
        val subject = intent.getStringExtra("subject").toString()

        binding.lbName.text = topName
        datePicker()

        database = FirebaseDatabase.getInstance().getReference("UserTeachers")
        database.child(userName).child("Subject").child(subject).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

                toast()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                var index = 0
                if (snapshot.exists()){

                    for (data in snapshot.children){
                        val sec = data.key.toString()
                        addButton(sec,subject,index)
                        index += 1
                    }
                }
            }
        })

        binding.icTeacher2MenuBar.setOnClickListener {

            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }

    private fun addButton(room : String,subject: String, index: Int){

        val button = Button(this)
        val space = Space(this)
        button.height = 150
        button.text = room
        button.setTextColor(Color.WHITE)
        button.textSize = 20F
        button.isAllCaps = false
        space.minimumHeight = 25
        button.background = button.context.resources.getDrawable(R.drawable.mt_button3)
        if(index%2 == 0) {
            binding.llTeacher2Layout.addView(button)
            binding.llTeacher2Layout.addView(space)
        }else{
            binding.llTeacher2Layout2.addView(button)
            binding.llTeacher2Layout2.addView(space)
        }

        button.setOnClickListener {

            val intent = Intent(this, TeacherClassMenu::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("subject", subject)
            intent.putExtra("room", room)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_exit,R.anim.anim_enter2)
        }
    }

    private fun toast(){

        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit2)
    }
}