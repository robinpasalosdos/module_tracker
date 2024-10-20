package com.example.moduletracker

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isInvisible
import com.example.moduletracker.databinding.ActivityStudentBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast

class Student : AppCompatActivity() {

    private lateinit var binding : ActivityStudentBinding
    private lateinit var database : DatabaseReference
    private lateinit var moduleDatabase : DatabaseReference
    private lateinit var title : String
    private lateinit var date : String
    private lateinit var time : String
    private lateinit var roomNo : String
    private lateinit var status: String
    private lateinit var link : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()
        val sub = listOf<String>(
                "English", "Math", "Science", "AP", "ESP", "Filipino", "TLE", "MAPEH"
        )

        val modules : ArrayList<String> = arrayListOf()
        val arrayAdapter = ArrayAdapter(this,R.layout.item,sub)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spSubjects.adapter = arrayAdapter
        binding.lbName.text = topName
        datePicker()

        binding.spSubjects.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                database = FirebaseDatabase.getInstance().getReference("UserStudents")
                moduleDatabase = FirebaseDatabase.getInstance().getReference("Modules")
                modules.clear()
                var index: Int = 0

                database.child(userName).child("Details").get().addOnSuccessListener {
                    val level = it.child("level").value.toString()
                    val section = it.child("section").value.toString()
                    val room = level.plus("-").plus(section)

                    moduleDatabase.child(room).child(sub[position]).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            toast()
                            noDetails()
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (data in snapshot.children) {
                                    val sec = data.key.toString()
                                    modules.add(sec)
                                }

                                moduleDatabase.child(room).child(sub[position]).child(modules[0]).get().addOnSuccessListener {

                                    binding.btnModuleName.text = modules[0]
                                    assignLbl(userName, modules, sub, position,index,it)
                                    binding.btnStudentLeft.text = ""
                                    if(modules.size <= 1) {

                                        binding.btnStudentRight.text = ""
                                    }else{

                                        binding.btnStudentRight.text = ">"
                                    }

                                    binding.btnStudentRight.setOnClickListener {

                                        if(index < modules.size - 1 && modules.isNotEmpty()){
                                            index += 1

                                            binding.btnModuleName.text = modules[index]
                                            if(index > 0){

                                                binding.btnStudentLeft.text = "<"
                                            }
                                            if(index > modules.size - 2){

                                                binding.btnStudentRight.text = ""
                                            }

                                            moduleDatabase.child(room).child(sub[position]).child(modules[index]).get().addOnSuccessListener {

                                                assignLbl(userName, modules, sub, position,index, it)

                                            }.addOnFailureListener {

                                                toast()
                                            }
                                        }
                                    }

                                    binding.btnStudentLeft.setOnClickListener {

                                        if(index > 0 && modules.isNotEmpty()){
                                            index -= 1

                                            binding.btnModuleName.text = modules[index]
                                            if(index < 1){

                                                binding.btnStudentLeft.text = ""
                                            }
                                            if(index < modules.size - 1){

                                                binding.btnStudentRight.text = ">"
                                            }

                                            moduleDatabase.child(room).child(sub[position]).child(modules[index]).get().addOnSuccessListener {

                                                assignLbl(userName, modules, sub, position,index ,it)

                                            }.addOnFailureListener {

                                                toast()
                                            }
                                        }
                                    }

                                }.addOnFailureListener {

                                    toast()
                                }

                            }else{

                                noDetails()
                            }
                        }
                    })

                }.addOnFailureListener {

                    toast()
                    noDetails()
                }
            }
        }

        binding.icStudentMenuBar.setOnClickListener {
            val intent = Intent(this, ActionBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("student", "student")
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }
    }
    private fun assignLbl(userName: String, modules: ArrayList<String>, sub: List<String>, position: Int,index: Int, it: DataSnapshot){

        title = "Title: ".plus(it.child("title").value.toString())
        date = "Due date: ".plus(it.child("date").value.toString())
        time = "Time: ".plus(it.child("time").value.toString())
        roomNo = "Room no.: ".plus(it.child("roomNo").value.toString())
        link = it.child("link").value.toString()

        binding.lblStudentTitle.text = title
        binding.lblStudentDate.text = date
        binding.lblStudentTime.text = time
        binding.lblStudentRoom.text = roomNo
        binding.tvStudentDetails.text = "Details"
        binding.tvStudentNoDetails.text = ""
        binding.btnStudentDownload.isInvisible = false

        binding.btnStudentDownload.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(link)
            startActivity(i)
        }
        database.child(userName).child("Subjects").child(sub[position]).child(modules[index]).get().addOnSuccessListener {
            status = it.child("status").value.toString()
            if (status == "null"){
                binding.lblStudentStatus.text = "Status: Missing"
            }else{
                binding.lblStudentStatus.text = "Status: ".plus(status)
            }
        }.addOnFailureListener {

            toast()
        }
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

    private fun noDetails(){

        binding.lblStudentTitle.text = ""
        binding.lblStudentDate.text = ""
        binding.lblStudentTime.text = ""
        binding.lblStudentRoom.text = ""
        binding.tvStudentDetails.text = ""
        binding.lblStudentStatus.text = ""
        binding.tvStudentNoDetails.text = "No Details"
        binding.btnStudentDownload.isInvisible = true
        binding.btnModuleName.text = "No Module"
        binding.btnStudentRight.text = ""
        binding.btnStudentLeft.text = ""
    }
}