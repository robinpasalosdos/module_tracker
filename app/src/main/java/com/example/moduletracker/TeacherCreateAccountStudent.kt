package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityTeacherCreateAccountStudentBinding
import com.google.firebase.database.*

class TeacherCreateAccountStudent : AppCompatActivity() {

    private lateinit var binding : ActivityTeacherCreateAccountStudentBinding
    private lateinit var database : DatabaseReference
    private lateinit var roomName : String
    private lateinit var subject : String
    private lateinit var topName : String
    private lateinit var teacherUserName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherCreateAccountStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherUserName = intent.getStringExtra("username").toString()
        topName = intent.getStringExtra("name").toString()
        subject = intent.getStringExtra("subject").toString()
        roomName = intent.getStringExtra("room").toString()

        binding.icStudentCaBack.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        val level = roomName.slice(0..0)
        val section = roomName.slice(2..2)
        binding.tvTcsLevel.text = "Year Level:      ".plus(level)
        binding.tvTcsSection.text = "Section:      ".plus(section)
        binding.btnCreate.setOnClickListener {

            val name = binding.edtStudentName.text.toString()
            val email = binding.edtStudentEmail.text.toString()
            val userName = binding.edtStudentUsername.text.toString()
            val password = binding.edtStudentPassword.text.toString()
            val phoneNoInEdt = binding.edtStudentPhoneNo.text.toString()
            val retypePassword = binding.edtStudentRetypePassword.text.toString()

            database = FirebaseDatabase.getInstance().getReference("UserStudents")

            if (name.isNotEmpty() && email.isNotEmpty() && userName.isNotEmpty() && password.isNotEmpty() && phoneNoInEdt.isNotEmpty() && retypePassword.isNotEmpty()) {
                database.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(error: DatabaseError) {

                        toast("Failed")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val phoneNo = "+63".plus(phoneNoInEdt.substring(1,phoneNoInEdt.length))
                        val user = UserStudents(email,name,password,phoneNo,level,section,userName)
                        if (userName.length > 5 || password.length > 5) {
                            var isCreate = true
                            if (snapshot.exists()) {
                                for (data in snapshot.children) {
                                    if (data.key.toString() == userName) {
                                        isCreate = false
                                        toast("Username exists!")
                                    }
                                }
                                if (isCreate) {
                                    createAccount(password, retypePassword, userName, user)
                                }
                            }
                        }else{

                            toast("Username or password must contains more than 6 characters!")
                        }
                    }
                })
            }else {

                toast("Required all fields!")
            }
        }
    }
    private fun createAccount(password: String, retypePassword: String, userName: String, user: UserStudents){

        if (password == retypePassword) {
            database.child(userName).child("Details").setValue(user).addOnSuccessListener {

                val intent = Intent(this, TeacherClassList::class.java)
                intent.putExtra("username", teacherUserName)
                intent.putExtra("name", topName)
                intent.putExtra("subject", subject)
                intent.putExtra("room", roomName)
                startActivity(intent)
                overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                toast("Successfully created!")
                finishAffinity()

            }.addOnFailureListener {

                toast("Failed")
            }
        } else {

            toast("Password didn't match!")
        }
    }

    private fun toast(text: String){

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
    }
}