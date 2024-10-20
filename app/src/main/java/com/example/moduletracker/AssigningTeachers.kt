package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityAssigningTeachersBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AssigningTeachers : AppCompatActivity() {
    private lateinit var binding: ActivityAssigningTeachersBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssigningTeachersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()

        binding.lbName.text = topName
        datePicker()

        binding.icAtMenuBar.setOnClickListener {

            val intent = Intent(this, HeadMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("head", "head2")
            startActivity(intent)
        }

        binding.btnTeacherAssigningSearch.setOnClickListener {

            val assignedUserName: String = binding.edtTeacherAssigning.text.toString()
            if (assignedUserName.isNotEmpty()) {

                database = FirebaseDatabase.getInstance().getReference("UserTeachers")
                database.child(userName).child("Details").get().addOnSuccessListener {

                    if (it.exists()){
                        Toast.makeText(this, "Username existed!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AssigningTeachersMenu::class.java)
                        intent.putExtra("assignedUsername", assignedUserName)
                        intent.putExtra("username", userName)
                        intent.putExtra("name", topName)
                        intent.putExtra("level", "7")
                        intent.putExtra("section", "1")
                        startActivity(intent)
                        overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                        finish()

                    }else{

                        Toast.makeText(this,"Invalid Username!",Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener{

                    Toast.makeText(this,"Failed!",Toast.LENGTH_SHORT).show()
                }
            }

            else {

                Toast.makeText(this, "Please Enter the Username", Toast.LENGTH_SHORT).show()
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