package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityCreateAccountTeacherBinding
import com.google.firebase.database.*

class CreateAccountTeacher : AppCompatActivity() {

    private lateinit var binding : ActivityCreateAccountTeacherBinding
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icTeacherCaBack.setOnClickListener {
            
            finish()
            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
        }

        binding.btnCreate.setOnClickListener {

            val name = binding.edtTeacherName.text.toString()
            val email = binding.edtTeacherEmail.text.toString()
            val userName = binding.edtTeacherUsername.text.toString()
            val password = binding.edtTeacherPassword.text.toString()
            val phoneNoInEdt = binding.edtTeacherPhoneNo.text.toString()
            val retypePassword = binding.edtTeacherRetypePassword.text.toString()

            database = FirebaseDatabase.getInstance().getReference("UserTeachers")


            if (name.isNotEmpty() && email.isNotEmpty() && userName.isNotEmpty() && password.isNotEmpty() && phoneNoInEdt.isNotEmpty() && retypePassword.isNotEmpty()) {
                database.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(error: DatabaseError) {

                        toast("Failed!")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val phoneNo = "+63".plus(phoneNoInEdt.substring(1, phoneNoInEdt.length))
                            val user = UserTeachers(email, name, password, phoneNo, userName)
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
                                        createAccount(
                                            password,
                                            retypePassword,
                                            userName,
                                            user,
                                            name
                                        )
                                    }
                                }
                            } else {

                                toast("Username or password must contains more than 6 characters!")
                            }
                        }
                    }
                })
            }else {

                toast("Required all fields!")
            }
        }
    }

    private fun createAccount(password: String, retypePassword: String, userName: String, user: UserTeachers, name: String){

        if (password == retypePassword) {
            database.child(userName).child("Details").setValue(user).addOnSuccessListener {

                val intent = Intent(this, Teacher::class.java)
                intent.putExtra("username", userName)
                intent.putExtra("name", name)
                startActivity(intent)
                overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                toast("Successfully created!")
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