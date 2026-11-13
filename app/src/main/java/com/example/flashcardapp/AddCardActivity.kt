package com.example.flashcardapp

import android.app.Activity
import android.content.Intent
import android.os.Build
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
            applyExitTransition()
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
            applyExitTransition()
        }
    }

    private fun applyExitTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.left_in, R.anim.right_out)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.left_in, R.anim.right_out)
        }
    }
}
