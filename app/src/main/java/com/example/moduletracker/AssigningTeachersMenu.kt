package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.moduletracker.databinding.ActivityAssigningTeachersMenuBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AssigningTeachersMenu : AppCompatActivity() {
    private lateinit var binding : ActivityAssigningTeachersMenuBinding
    private lateinit var database : DatabaseReference
    private lateinit var checkboxes: List<CheckBox>
    private lateinit var subjects: List<String>
    private lateinit var level: String
    private lateinit var section: String
    private lateinit var sec: String
    private lateinit var userName: String
    private lateinit var assignedUserName: String
    private lateinit var topName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssigningTeachersMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assignedUserName = intent.getStringExtra("assignedUsername").toString()
        userName = intent.getStringExtra("username").toString()
        topName = intent.getStringExtra("name").toString()
        level = intent.getStringExtra("level").toString()
        section = intent.getStringExtra("section").toString()

        checkboxes = listOf<CheckBox>(
                binding.rbEnglish,
                binding.rbMath,
                binding.rbScience,
                binding.rbAp,
                binding.rbEsp,
                binding.rbFilipino,
                binding.rbTle,
                binding.rbMapeh
        )

        subjects = listOf<String>(
                "English",
                "Math",
                "Science",
                "AP",
                "ESP",
                "Filipino",
                "TLE",
                "MAPEH"
        )

        binding.lbName.text = topName
        datePicker()

        binding.icAtmMenuBar.setOnClickListener {
            val intent = Intent(this, HeadMenuBar::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out, R.anim.vanish_in)
        }

        val levelList = listOf<String>(
            "7", "8", "9", "10"
        )
        val arrayAdapterLevel = ArrayAdapter(this, R.layout.item2 , levelList)
        arrayAdapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spAssigningTeacherLevel.adapter = arrayAdapterLevel

        val sectionList = listOf<String>(
            "1","2","3")
        val arrayAdapterSection = ArrayAdapter(this, R.layout.item2, sectionList)
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spAssigningTeacherSection.adapter = arrayAdapterSection

        binding.spAssigningTeacherLevel.setSelection(level.toInt() - 7)
        binding.spAssigningTeacherSection.setSelection(section.toInt() - 1)
        getData(binding.spAssigningTeacherLevel)
        getData(binding.spAssigningTeacherSection)

    }

    private fun getData(spinner: Spinner){

        level = binding.spAssigningTeacherLevel.selectedItem.toString()
        section = binding.spAssigningTeacherSection.selectedItem.toString()
        sec = level.plus("-".plus(section))
        database = FirebaseDatabase.getInstance().getReference("UserTeachers")
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                level = binding.spAssigningTeacherLevel.selectedItem.toString()
                section = binding.spAssigningTeacherSection.selectedItem.toString()
                sec = level.plus("-".plus(section))
                getSubjectData()
            }
        }
    }

    private fun getSubjectData(){

        for (index2 in 0 until checkboxes.count()) {

            checkboxes[index2].isChecked = false
            checkboxes[index2].isEnabled = true
        }

        database.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

                toast()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(teacherUser in snapshot.children){
                        val teacherUserName = teacherUser.key.toString()
                        for(teacherUserSubject in snapshot.child(teacherUserName).child("Subject").children){
                            val teacherSubject = teacherUserSubject.key.toString()
                            for (teacherUserSection in snapshot.child(teacherUserName).child("Subject").child(teacherSubject).children){
                                val teacherSection = teacherUserSection.key.toString()
                                if (teacherSection == sec){
                                    for (ind in 0 until checkboxes.count()) {
                                        if (teacherSubject == subjects[ind]) {
                                            checkboxes[ind].isChecked = true
                                            checkboxes[ind].isEnabled = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
        binding.btnAssigningAdd.setOnClickListener {
            val user = Test(level.plus("-".plus(section)))

            when {
                !binding.rbEnglish.isChecked && !binding.rbMath.isChecked && !binding.rbScience.isChecked && !binding.rbAp.isChecked && !binding.rbEsp.isChecked && !binding.rbFilipino.isChecked && !binding.rbTle.isChecked && !binding.rbMapeh.isChecked -> {
                    Toast.makeText(this, "Required at least one subject!", Toast.LENGTH_SHORT).show()
                }
            }

            for (index in 0 until checkboxes.count()) {
                updateSubjects(checkboxes[index], assignedUserName, subjects[index], sec, user)
            }

            Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AssigningTeachersMenu::class.java)
            intent.putExtra("assignedUsername", assignedUserName)
            intent.putExtra("username", userName)
            intent.putExtra("name", topName)
            intent.putExtra("level", level)
            intent.putExtra("section", section)
            finish()
            startActivity(intent)
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)

        }
    }


    private fun updateSubjects(radioButton : CheckBox, userName: String, subject : String, sec : String,  user : Test){

        if (radioButton.isChecked && radioButton.isEnabled){

            database.child(userName).child("Subject").child(subject).child(sec).setValue(user).addOnSuccessListener {
                radioButton.isChecked = false
            }.addOnFailureListener {

                toast()
            }
        }
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
        overridePendingTransition(R.anim.vanish_out, R.anim.vanish_in)
    }
}