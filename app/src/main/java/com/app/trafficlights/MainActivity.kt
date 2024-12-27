package com.app.trafficlights

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val redLight = findViewById<View>(R.id.redLight)
        val yellowLight = findViewById<View>(R.id.yellowLight)
        val greenLight = findViewById<View>(R.id.greenLight)
        val firstNumber = findViewById<ImageView>(R.id.firstNumber)
        val secondNumber = findViewById<ImageView>(R.id.secondNumber)
        findViewById<CardView>(R.id.cvSetting).setOnClickListener {
            Intent(this, MainActivity2::class.java).also {
                startActivity(it)
            }
        }

        val handler = Handler(Looper.getMainLooper())

        findViewById<LinearLayout>(R.id.cvtraffic).setOnClickListener {
            findViewById<CardView>(R.id.cvSetting).visibility = View.VISIBLE
            handler.postDelayed({
                findViewById<CardView>(R.id.cvSetting).visibility = View.GONE
            }, 3000)

        }
        val lights = listOf(redLight, yellowLight, greenLight)
        var currentIndex = 0
        var time = 0
        fun setNumberImages(number: Int) {
            val numberString = number.toString().padStart(2, '0')

            val firstDigit = numberString[0]
            val secondDigit = numberString[1]

            val digitToDrawable = mapOf(
                '0' to R.drawable.test_0,
                '1' to R.drawable.test_1,
                '2' to R.drawable.test_2,
                '3' to R.drawable.test_3,
                '4' to R.drawable.test_4,
                '5' to R.drawable.test_5,
                '6' to R.drawable.test_6,
                '7' to R.drawable.test_7,
                '8' to R.drawable.test_8,
                '9' to R.drawable.test_9
            )
            secondNumber.setImageResource(digitToDrawable[firstDigit] ?: R.drawable.test_off)
            firstNumber.setImageResource(digitToDrawable[secondDigit] ?: R.drawable.test_off)
        }

        fun updateLights(): Int {
            lights.forEach { it.setBackgroundResource(R.drawable.light_circle_grey) }
            var test = Thread{
                for (currentNumber in 0..20) {
                    var current = 20 -currentNumber
                    setNumberImages(current)
                    Thread.sleep(1000)
                }

            }
            var test1=Thread{
                for (currentNumber in 0..3) {
                    setNumberImages(0)
                    Thread.sleep(1000)
                }

            }
            var test2=Thread{
                for (currentNumber in 0..20) {
                    var current = 20 - currentNumber
                    setNumberImages(current)
                    Thread.sleep(1000)
                }

            }
            when (currentIndex) {
                0 -> {
                    lights[currentIndex].setBackgroundResource(R.drawable.light_circle_red)
                    time = 21000
                    test.start()
                    test1.interrupt()
                    test2.interrupt()


                }

                1 -> {
                    lights[currentIndex].setBackgroundResource(R.drawable.light_circle_yellow)
                    time = 4000
                    test.interrupt()
                    test1.start()
                    test2.interrupt()
                }

                2 -> {
                    lights[currentIndex].setBackgroundResource(R.drawable.light_circle_green)
                    time = 21000
                    test.interrupt()
                    test1.interrupt()
                    test2.start()
                }
            }

            currentIndex = (currentIndex + 1) % lights.size
            return time
        }
        handler.postDelayed(object : Runnable {
            override fun run() {
                var delay = updateLights()
                handler.postDelayed(this, delay.toLong())
            }
        }, 2000)
    }
}


