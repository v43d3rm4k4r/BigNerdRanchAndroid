package com.bignardranch.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

/**
 * @see android.widget.Toast.setGravity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        trueButton.setOnClickListener {
            Toast.makeText(
                this,
                R.string.correct_toast,
                Toast.LENGTH_SHORT
            ).show()
        }

        falseButton.setOnClickListener {
            Toast.makeText(
                this,
                R.string.incorrect_toast,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}