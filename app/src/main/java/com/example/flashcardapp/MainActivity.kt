package com.example.flashcardapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val flashcardQuestion = findViewById<View>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<View>(R.id.flashcard_answer)
        val rep1 = findViewById<View>(R.id.rep1)
        val rep2 = findViewById<View>(R.id.rep2)
        val rep3 = findViewById<View>(R.id.rep3)
        val answersContainer = findViewById<View>(R.id.answers_container)
        val icon = findViewById<ImageView>(R.id.toggle_choices_visibility)
        var isShowingAnswers = true

        icon.setOnClickListener {
            if (isShowingAnswers) {
                // Hide the block
                answersContainer.visibility = View.GONE
                icon.setImageResource(R.drawable.show_icon)
                isShowingAnswers = false
            } else {
                // Reveal the block
                answersContainer.visibility = View.VISIBLE
                icon.setImageResource(R.drawable.hide_icon)
                isShowingAnswers = true
            }
        }

        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
        }
        flashcardAnswer.setOnClickListener {
            flashcardQuestion.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.INVISIBLE
        }

        rep1.setOnClickListener {
            rep1.setBackgroundColor(getResources().getColor(R.color.red, null))
            rep3.setBackgroundColor(getResources().getColor(R.color.green, null))
        }
        rep2.setOnClickListener {
            rep2.setBackgroundColor(getResources().getColor(R.color.red, null))
            rep3.setBackgroundColor(getResources().getColor(R.color.green, null))
        }
        rep3.setOnClickListener {
            rep3.setBackgroundColor(getResources().getColor(R.color.green, null))
        }
    }
}