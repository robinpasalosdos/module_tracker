package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logo = findViewById<ImageView>(R.id.imageView)
        val teacherButton = findViewById<Button>(R.id.btn_teacher)
        val studentButton = findViewById<Button>(R.id.btn_student)


        logo.animate().apply {
            duration = 1000
            rotationYBy(-720f)
            rotationX(-360f)
            scaleYBy(83f)
            scaleXBy(83f)
            alpha(1f)
        }
        teacherButton.setOnClickListener {
            
            val intent = Intent(this, TeacherLogin::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_up,R.anim.scale_out)
        }

        studentButton.setOnClickListener {
            
            val intent = Intent(this, StudentLogin::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_up,R.anim.scale_out)
        }
    }

}