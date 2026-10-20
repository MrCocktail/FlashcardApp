package com.example.flashcardapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.add_card_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val questionEditText = findViewById<EditText>(R.id.question)
        val answerEditText = findViewById<EditText>(R.id.answer)
        val cancelButton = findViewById<ImageView>(R.id.cancel_button)
        val saveButton = findViewById<ImageView>(R.id.save_button)

        cancelButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            val questionText = questionEditText.text.toString()
            val answerText = answerEditText.text.toString()

            val data = Intent().apply {
                putExtra("question_key", questionText)
                putExtra("answer_key", answerText)
                putExtra("QUESTION_KEY", questionText)
                putExtra("ANSWER_KEY", answerText)
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
