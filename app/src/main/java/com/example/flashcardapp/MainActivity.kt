package com.example.flashcardapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var flashcardDatabase: FlashcardDatabase
    private var allFlashcards = mutableListOf<Flashcard>()
    private var currentCardDisplayedIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        flashcardDatabase = FlashcardDatabase(this)
        flashcardDatabase.initFirstCard()
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        val rep1 = findViewById<TextView>(R.id.rep1)
        val rep2 = findViewById<TextView>(R.id.rep2)
        val rep3 = findViewById<TextView>(R.id.rep3)
        val answersContainer = findViewById<View>(R.id.answers_container)
        val icon = findViewById<ImageView>(R.id.toggle_choices_visibility)
        val addCard = findViewById<ImageView>(R.id.add_card)
        val nextCardButton = findViewById<ImageView>(R.id.next_card_button)

        // Afficher la première carte si la base de données contient des cartes
        if (allFlashcards.isNotEmpty()) {
            val firstCard = allFlashcards[0]
            flashcardQuestion.text = firstCard.question
            flashcardAnswer.text = firstCard.answer
            if (firstCard.wrongAnswer1 != null && firstCard.wrongAnswer2 != null) {
                rep1.text = firstCard.wrongAnswer1
                rep2.text = firstCard.wrongAnswer2
                rep3.text = firstCard.answer
            }
        }

        var isShowingAnswers = true

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val question = data.getStringExtra("QUESTION_KEY") ?: data.getStringExtra("question_key")
                    val answer = data.getStringExtra("ANSWER_KEY") ?: data.getStringExtra("answer_key")

                    if (!question.isNullOrEmpty() && !answer.isNullOrEmpty()) {
                        // Insérer la nouvelle carte dans la base de données
                        flashcardDatabase.insertCard(Flashcard(question, answer))

                        // Mettre à jour la liste complète des cartes depuis la base
                        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                        currentCardDisplayedIndex = allFlashcards.size - 1

                        // Mettre à jour l'affichage avec la nouvelle carte
                        flashcardQuestion.text = question
                        flashcardAnswer.text = answer

                        rep1.setBackgroundColor(android.graphics.Color.parseColor("#ffee8c"))
                        rep2.setBackgroundColor(android.graphics.Color.parseColor("#ffee8c"))
                        rep3.setBackgroundColor(android.graphics.Color.parseColor("#ffee8c"))

                        flashcardQuestion.visibility = View.VISIBLE
                        flashcardAnswer.visibility = View.INVISIBLE
                    }
                }
            }
        }

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

        addCard.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

        nextCardButton.setOnClickListener {
            // Ne rien afficher de nouveau s'il n'y a qu'une seule carte (ou aucune)
            if (allFlashcards.size <= 1) {
                return@setOnClickListener
            }

            // Passer à la carte suivante
            currentCardDisplayedIndex++

            // Reboucler au début si on a atteint la fin de la liste
            if (currentCardDisplayedIndex >= allFlashcards.size) {
                currentCardDisplayedIndex = 0
            }

            // Récupérer et afficher la nouvelle carte
            val currentCard = allFlashcards[currentCardDisplayedIndex]
            flashcardQuestion.text = currentCard.question
            flashcardAnswer.text = currentCard.answer

            // Mettre à jour les choix si présents
            if (currentCard.wrongAnswer1 != null && currentCard.wrongAnswer2 != null) {
                rep1.text = currentCard.wrongAnswer1
                rep2.text = currentCard.wrongAnswer2
                rep3.text = currentCard.answer
            }

            // Réinitialiser la couleur des réponses
            rep1.setBackgroundColor(android.graphics.Color.parseColor("#ffee8c"))
            rep2.setBackgroundColor(android.graphics.Color.parseColor("#ffee8c"))
            rep3.setBackgroundColor(android.graphics.Color.parseColor("#ffee8c"))

            // Réinitialiser la visibilité (question visible, réponse masquée)
            flashcardQuestion.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.INVISIBLE
        }
    }
}