package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityStudentListBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StudentList : AppCompatActivity() {
    private lateinit var binding : ActivityStudentListBinding
    private lateinit var database : DatabaseReference
    private lateinit var mdatabase : DatabaseReference
    private lateinit var studentNames: ArrayList<String>
    private lateinit var studentUserName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()
        val subject = intent.getStringExtra("subject").toString()
        val roomName = intent.getStringExtra("room").toString()
        val moduleName = intent.getStringExtra("moduleName").toString()

        binding.lbName.text = topName
        studentNames = arrayListOf()
        datePicker()

        binding.icSlMenuBar.setOnClickListener {

            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance().getReference("UserStudents")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

                toast()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                studentNames.clear()
                if (snapshot.exists()){

                    for (data in snapshot.children){
                        studentUserName = data.key.toString()
                        val name = data.child("Details").child("name").value.toString()
                        val sec = data.child("Details").child("level").value.toString().plus("-".plus(data.child("Details").child("section").value.toString()))
                        if(roomName == sec){
                            studentNames.add(name)
                            addTextView(userName,subject,roomName,moduleName,name,studentUserName)
                        }
                    }

                }
            }
        })
    }

    private fun addTextView(userName : String, subject : String,roomName: String, moduleName : String ,name: String, studentUserName: String){

        val space = Space(this)
        val spaceTop = Space(this)
        val textView = TextView(this)
        val checkBox = CheckBox(this)
        checkBox.text = name
        checkBox.textSize = 18F
        binding.llStudentList.addView(checkBox)

        textView.textSize = 18F
        space.minimumHeight = 20
        spaceTop.minimumHeight = 5
        database = FirebaseDatabase.getInstance().getReference("UserTeachers")
        database.child(userName).child("Subject").child(subject).child(roomName).child(moduleName).get().addOnSuccessListener {
            val dueDate = it.child("date").value.toString()
            val dueTime = it.child("time").value.toString()
            val y = dueDate.slice(6..9)
            val m = dueDate.slice(0..1)
            val d = dueDate.slice(3..4)
            val min = dueTime.slice(0..1)
            val h = dueTime.slice(3..4)

            val calendar: Calendar = Calendar.getInstance()
            val dateFormat: SimpleDateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm")
            val dateTime: String = dateFormat.format(calendar.time).toString()

            val dy = dateTime.slice(6..9)
            val dm = dateTime.slice(0..1)
            val dd = dateTime.slice(3..4)
            val dmin = dateTime.slice(11..12)
            val dh = dateTime.slice(14..15)

            val modifiedDueDate = y.plus(m.plus(d.plus(min.plus(h))))
            val modifiedDateTime = dy.plus(dm.plus(dd.plus(dmin.plus(dh))))

            mdatabase = FirebaseDatabase.getInstance().getReference("UserStudents")
            checkBox.setOnCheckedChangeListener { _, isChecked ->

                val status: String
                val checked: Boolean
                if (isChecked) {
                    status = "Passed"
                    checked = true
                } else {
                    status = "Missing"
                    checked = false
                }
                val user = ModuleStatus(checked, status)
                mdatabase.child(studentUserName).child("Subjects").child(subject).child(moduleName).setValue(user).addOnSuccessListener {

                }.addOnFailureListener{

                    toast()
                }

            }
            mdatabase.child(studentUserName).child("Subjects").child(subject).child(moduleName).get().addOnSuccessListener {

                checkBox.isChecked = it.child("checked").value.toString().toBoolean()
                if (modifiedDateTime >= modifiedDueDate) {
                    var status: String
                    var checked: Boolean
                    val statusIn = it.child("status").value.toString()
                    if (statusIn == "Passed"){
                        textView.text = "Passed"
                        checkBox.isClickable = false
                    }else{
                        if (!checkBox.isChecked) {
                            textView.text = "Missing"
                            status = "Missing"
                            checked = false
                        }else {
                            textView.text = "Late"
                            status = "Late"
                            checked = true
                        }
                        val user = ModuleStatus(checked, status)
                        mdatabase.child(studentUserName).child("Subjects").child(subject).child(moduleName).setValue(user).addOnSuccessListener {

                        }.addOnFailureListener {

                            toast()
                        }

                        checkBox.setOnCheckedChangeListener { _, isChecked ->

                            if (isChecked) {
                                status = "Late"
                                checked = true

                            } else {
                                status = "Missing"
                                checked = false
                            }
                            textView.text = status
                            val userClass = ModuleStatus(checked, status)
                            mdatabase.child(studentUserName).child("Subjects").child(subject).child(moduleName).setValue(userClass).addOnSuccessListener {

                            }.addOnFailureListener {

                                toast()
                            }
                        }
                    }
                }
            }.addOnFailureListener{

                toast()
            }
        }.addOnFailureListener {

            toast()
        }

        binding.llStudentList2.addView(spaceTop)
        binding.llStudentList2.addView(space)
        binding.llStudentList2.addView(textView)
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat : SimpleDateFormat = SimpleDateFormat("E MM/dd/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }

    private fun toast(){

        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
    }
}