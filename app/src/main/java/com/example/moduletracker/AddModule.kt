package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityAddModuleBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddModule : AppCompatActivity() {
    private lateinit var binding : ActivityAddModuleBinding
    private lateinit var database : DatabaseReference
    private lateinit var moduleDatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("username").toString()
        val topName = intent.getStringExtra("name").toString()
        val subject = intent.getStringExtra("subject").toString()
        val roomName = intent.getStringExtra("room").toString()
        binding.lbName.text = topName
        datePicker()

        binding.icAddModuleMenuBar.setOnClickListener {

            val intent = Intent(this, TeacherMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnAddModuleAdd.setOnClickListener {

            val moduleName : String = binding.edtAddModuleName.text.toString()
            val title : String = binding.edtAddModuleTitle.text.toString()
            val date : String = binding.edtAddModuleDate.text.toString()
            val time : String = binding.edtAddModuleTime.text.toString()
            val room : String = binding.edtAddModuleRoomNo.text.toString()
            val link : String = binding.edtAddModuleLink.text.toString()
            database = FirebaseDatabase.getInstance().getReference("UserTeachers")
            moduleDatabase = FirebaseDatabase.getInstance().getReference("Modules")
            val user = Modules(moduleName,title,date,time,room,link)

            if(moduleName.isNotEmpty() && title.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && room.isNotEmpty() && link.isNotEmpty()) {

                database.child(userName).child("Subject").child(subject).child(roomName).child(moduleName).setValue(user).addOnSuccessListener {

                    val intent = Intent(this, ModuleMenu::class.java)
                    intent.putExtra("username", userName)
                    intent.putExtra("name", topName)
                    intent.putExtra("subject", subject)
                    intent.putExtra("room", roomName)
                    startActivity(intent)
                    overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                    finish()
                }.addOnFailureListener {

                    Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
                }

                moduleDatabase.child(roomName).child(subject).child(moduleName).setValue(user).addOnSuccessListener {

                    Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {

                    Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
                }

            }else{

                Toast.makeText(this, "Required all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun datePicker(){

        val calendar : Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E dd/MM/yyyy KK:mm aaa")
        val dateTime: String = dateFormat.format(calendar.time).toString()
        binding.lbDate.text = dateTime
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
    }
}