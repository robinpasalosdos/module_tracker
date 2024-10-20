package com.example.moduletracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isInvisible

class Intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val mtLogo = findViewById<ImageView>(R.id.mt_logo)
        val mtLogo1 = findViewById<ImageView>(R.id.mt_logo1)

        mtLogo1.animate().apply {
            alpha(0f)
        }

        mtLogo.animate().apply {
            alpha(0f)
        }.withEndAction {
            mtLogo.isInvisible = false
            mtLogo1.isInvisible = false
            mtLogo1.animate().apply {
                scaleYBy(170f)
                scaleXBy(170f)
            }
            mtLogo.animate().apply {
                duration = 2000
                scaleYBy(170f)
                scaleXBy(170f)
                alpha(1f)
            }.withEndAction {
                mtLogo.animate().apply {
                    duration = 1000
                    alpha(0f)
                }
                mtLogo1.animate().apply {
                    duration = 1500
                    alpha(1f)
                }.withEndAction {
                    mtLogo1.animate().apply {
                        duration = 2000
                        rotationYBy(720f)
                        rotationX(360f)
                    }.withEndAction {
                        mtLogo1.animate().apply {
                            duration = 1000
                            rotationYBy(720f)
                            rotationX(360f)
                            scaleYBy(-170f)
                            scaleXBy(-170f)
                            alpha(0f)
                        }.withEndAction {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.vanish_out,R.anim.vanish_in)
                            finishAffinity()
                        }
                    }
                }
            }
        }
    }
}