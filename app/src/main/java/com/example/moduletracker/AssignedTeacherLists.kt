package com.example.moduletracker

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityAssignedTeacherListsBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AssignedTeacherLists : AppCompatActivity() {
    private lateinit var binding: ActivityAssignedTeacherListsBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignedTeacherListsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()
        val subject = intent.getStringExtra("subject").toString()
        val sections : ArrayList<String> = arrayListOf()

        binding.lbName.text = topName
        datePicker()

        database = FirebaseDatabase.getInstance().getReference("UserTeachers")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

                toast()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                sections.clear()
                if (snapshot.exists()){

                    for (data in snapshot.children){
                        val name = data.child("Details").child("name").value.toString()
                        val subjectDb = data.child("Subject").child(subject).key.toString()
                        if (subjectDb == subject) {
                            for (dataSubject in data.child("Subject").child(subject).children) {
                                val section = dataSubject.key.toString()
                                sections.add(name.plus(" - ").plus(section))
                            }
                        }
                    }
                    sections.sort()
                    for (sec in sections){
                        addTeachers(sec)
                    }
                }
            }
        })

        binding.icAslMenuBar.setOnClickListener {
            val intent = Intent(this, HeadMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
        }
    }

    private fun addTeachers(sec: String){

        val sectionTv = TextView(this)
        val space = Space(this)
        sectionTv.text = sec
        sectionTv.setTextColor(Color.BLACK)
        sectionTv.textSize = 16F
        space.minimumHeight = 15
        binding.llAssignTeacherLists.addView(sectionTv)
        binding.llAssignTeacherLists.addView(space)
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }
    private fun toast(){

        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
    }
}