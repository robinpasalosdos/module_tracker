package com.example.moduletracker

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.widget.CompoundButtonCompat
import com.example.moduletracker.databinding.ActivityTeacherClassListBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TeacherClassList : AppCompatActivity() {
    private lateinit var binding : ActivityTeacherClassListBinding
    private lateinit var database : DatabaseReference
    private lateinit var studentNames: MutableMap<String,String>
    private lateinit var checkBoxes: ArrayList<CheckBox>
    private lateinit var studentUserName : String
    private lateinit var subject : String
    private lateinit var roomName : String
    private lateinit var topName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherClassListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        topName = intent.getStringExtra("name").toString()
        subject = intent.getStringExtra("subject").toString()
        roomName = intent.getStringExtra("room").toString()
        studentNames = mutableMapOf()
        checkBoxes = arrayListOf()
        binding.lbName.text = topName
        datePicker()

        binding.btnTclStudentAdd.setOnClickListener {

            val intent = Intent(this, TeacherCreateAccountStudent::class.java)
            intent.putExtra("subject", subject)
            intent.putExtra("room", roomName)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.icTeacherClMenuBar.setOnClickListener {

            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnTclStudentRemove.setOnClickListener {

            for (index in 0 until checkBoxes.count()) {
                if (checkBoxes[index].isChecked) {
                    val nameFromCb = checkBoxes[index].text
                    database = FirebaseDatabase.getInstance().getReference("UserStudents")
                    database.child(studentNames[nameFromCb].toString()).removeValue().addOnSuccessListener {

                        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, TeacherClassList::class.java)
                        intent.putExtra("username", userName)
                        intent.putExtra("name", topName)
                        intent.putExtra("subject", studentUserName)
                        intent.putExtra("room", roomName)
                        startActivity(intent)
                        overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                        finish()
                    }.addOnFailureListener {

                        toast()
                    }
                }
            }
        }

        database = FirebaseDatabase.getInstance().getReference("UserStudents")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                toast()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (data in snapshot.children){
                        studentUserName = data.key.toString()
                        val name = data.child("Details").child("name").value.toString()
                        val sec = data.child("Details").child("level").value.toString().plus("-".plus(data.child("Details").child("section").value.toString()))
                        if(roomName == sec){
                            studentNames[name] = studentUserName
                        }
                    }
                    studentNames.toSortedMap()
                    for (studentName in studentNames){

                        addTextView(studentName.key,studentName.value,userName)
                    }
                }
            }
        })
    }


    private fun addTextView(name: String,student: String, userName: String){

        val space = Space(this)
        val checkBox = CheckBox(this)
        checkBox.text = name
        checkBox.textSize = 18F
        checkBox.setTextColor(Color.BLACK)
        space.minimumHeight = 5
        checkBox.setBackgroundColor(Color.TRANSPARENT)
        checkBox.isClickable = false
        checkBoxes.add(checkBox)

        checkBox.setOnClickListener {
            for (index in 0 until checkBoxes.count())  {
                checkBoxes[index].setOnClickListener {

                    if (binding.btnTclStudentAdd.isEnabled) {
                        val stringFromCb = checkBoxes[index].text
                        val intent = Intent(this, TeacherAccountManagementStudent::class.java)
                        intent.putExtra("username", userName)
                        intent.putExtra("studentUsername", studentNames[stringFromCb].toString())
                        intent.putExtra("name", topName)
                        startActivity(intent)
                    }
                }
            }
        }
        checkBox.setOnLongClickListener {

            for (index in 0 until checkBoxes.count()) {
                if (checkBoxes[index].isChecked){

                    checkBoxes[index].isChecked = false
                }
            }
            binding.vwTcl.setBackgroundColor(Color.TRANSPARENT)
            binding.btnTclStudentAdd.isInvisible = true
            binding.btnTclStudentAdd.isEnabled = false
            binding.btnTclStudentRemove.isInvisible = false
            binding.btnTclStudentRemove.isEnabled = true
            true
        }
        
        binding.llTeacherClassList.setOnClickListener{

            binding.vwTcl.setBackgroundColor(checkBox.context.resources.getColor(R.color.background))
            binding.btnTclStudentAdd.isInvisible = false
            binding.btnTclStudentAdd.isEnabled = true
            binding.btnTclStudentRemove.isInvisible = true
            binding.btnTclStudentRemove.isEnabled = false
        }

        binding.llTeacherClassList.addView(checkBox)
        binding.llTeacherClassList.addView(space)
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }

    private fun toast(){

        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit2)
    }
}