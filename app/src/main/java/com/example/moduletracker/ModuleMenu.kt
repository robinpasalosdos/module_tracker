package com.example.moduletracker

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.updatePadding
import com.example.moduletracker.databinding.ActivityModuleMenuBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ModuleMenu : AppCompatActivity() {
    private lateinit var binding : ActivityModuleMenuBinding
    private lateinit var database : DatabaseReference
    private lateinit var userName : String
    private lateinit var topName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("username").toString()
        topName = intent.getStringExtra("name").toString()
        val subject = intent.getStringExtra("subject").toString()
        val roomName = intent.getStringExtra("room").toString()

        binding.lbName.text = topName
        datePicker()
        database = FirebaseDatabase.getInstance().getReference("UserTeachers")
        database.child(userName).child("Subject").child(subject).child(roomName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                toast("Failed")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val moduleName = data.key.toString()
                        if (moduleName != "section"){
                            val date = data.child("date")
                            val dueDate = date.value.toString()
                            addTextView(moduleName, dueDate, subject, roomName)

                            if (!date.exists()) {

                                binding.tvModule.text = "No Module"
                            }
                        }
                    }
                }
            }
        })

        binding.icModuleMenuBar.setOnClickListener {

            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnModuleAdd.setOnClickListener {

            val intent = Intent(this, AddModule::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("subject", subject)
            intent.putExtra("room", roomName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }
    }

    private fun addTextView(moduleName: String,dueDate: String, subject : String, roomName : String) {

        val view = View(this)
        val moduleTextView = TextView(this)
        val passedTextView = TextView(this)
        val space = Space(this)
        space.minimumHeight = 15
        view.layoutParams = LinearLayout.LayoutParams(1000,2)
        view.setBackgroundColor(Color.BLACK)
        moduleTextView.text = moduleName
        moduleTextView.textSize = 18F
        moduleTextView.setTextColor(Color.BLACK)
        moduleTextView.updatePadding(left = 5)
        passedTextView.setTextColor(Color.BLACK)
        passedTextView.text = dueDate
        passedTextView.updatePadding(left = 7)
        passedTextView.textSize = 14F

        binding.llModuleMenu.addView(moduleTextView)
        binding.llModuleMenu.addView(passedTextView)
        binding.llModuleMenu.addView(view)
        binding.llModuleMenu.addView(space)

        moduleTextView.setOnClickListener {

            val intent = Intent(this, StudentList::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("subject", subject)
            intent.putExtra("room", roomName)
            intent.putExtra("moduleName", moduleName)
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

    private fun toast(text: String){

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit2)
    }
}