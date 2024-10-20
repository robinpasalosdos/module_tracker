package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityCreateAccountStudentBinding
import com.google.firebase.database.*

class CreateAccountStudent : AppCompatActivity() {

    private lateinit var binding : ActivityCreateAccountStudentBinding
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val levelList = listOf<String>(
                "7", "8", "9", "10"
        )
        val arrayAdapterLevel = ArrayAdapter(this, R.layout.item2, levelList)
        arrayAdapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spLevel.adapter = arrayAdapterLevel

        val sectionList = listOf<String>(
                "1","2","3")
        val arrayAdapterSection = ArrayAdapter(this, R.layout.item2,sectionList)
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSection.adapter = arrayAdapterSection

        binding.icStudentCaBack.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnCreate.setOnClickListener {

            val level = binding.spLevel.selectedItem.toString()
            val section = binding.spSection.selectedItem.toString()
            val name = binding.edtStudentName.text.toString()
            val email = binding.edtStudentEmail.text.toString()
            val userName = binding.edtStudentUsername.text.toString()
            val password = binding.edtStudentPassword.text.toString()
            val phoneNoInEdt = binding.edtStudentPhoneNo.text.toString()

            val retypePassword = binding.edtStudentRetypePassword.text.toString()

            database = FirebaseDatabase.getInstance().getReference("UserStudents")

            if (name.isNotEmpty() && email.isNotEmpty() && userName.isNotEmpty() && password.isNotEmpty() && phoneNoInEdt.isNotEmpty() && retypePassword.isNotEmpty()) {
                database.addListenerForSingleValueEvent(object : ValueEventListener{

                    override fun onCancelled(error: DatabaseError) {

                        toast("Failed!")
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
                                    createAccount(password, retypePassword, userName, user, name)
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
    private fun createAccount(password: String, retypePassword: String, userName: String, user: UserStudents, name: String){

        if (password == retypePassword) {
            database.child(userName).child("Details").setValue(user).addOnSuccessListener {

                val intent = Intent(this, Student::class.java)
                intent.putExtra("username", userName)
                intent.putExtra("name", name)
                startActivity(intent)
                overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                Toast.makeText(this, "Successfully created!", Toast.LENGTH_SHORT).show()
                finishAffinity()

            }.addOnFailureListener {

                toast("Failed!")
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