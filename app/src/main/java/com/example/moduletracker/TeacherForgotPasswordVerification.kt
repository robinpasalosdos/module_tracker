package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduletracker.databinding.ActivityTeacherForgotPasswordVerificationBinding

class TeacherForgotPasswordVerification : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherForgotPasswordVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherForgotPasswordVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val code = intent.getStringExtra("code").toString()
        val userName = intent.getStringExtra("username").toString()

        binding.icTeacherFpvBackA.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit2)
        }

        binding.btnTeacherFpvApply.setOnClickListener {

            val codeInEdt = binding.edtTeacherFpvCode.text.toString()
            if(code == codeInEdt && codeInEdt.isNotEmpty()){

                val intent = Intent(this, TeacherForgotPasswordChange::class.java)
                intent.putExtra("username", userName)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_exit,R.anim.anim_enter2)
            }else{
                Toast.makeText(this, "Wrong code!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit2)
    }
}