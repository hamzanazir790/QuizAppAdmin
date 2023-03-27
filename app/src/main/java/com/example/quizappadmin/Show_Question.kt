package com.example.quizappadmin

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizappadmin.databinding.ActivityShowQuestionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


private lateinit var binding: ActivityShowQuestionBinding
val database = FirebaseDatabase.getInstance().reference
private lateinit var currentUserID: String
var score = 0
var totalScore = 0
private lateinit var questionList: List<Question>
private var currentQuestionIndex = 0

class Show_Question : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get the current user's ID (or some other unique identifier)
        currentUserID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
// Fetch all questions from Firebase
        val databaseRef = FirebaseDatabase.getInstance().getReference("questions")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    questionList = snapshot.children.mapNotNull { it.getValue(Question::class.java) }
                    // Display the first question
                    displayQuestion()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
        binding.score.text= score.toString()
        binding.opt1.setOnClickListener {
            updateScore(9)
            displayQuestion()}
        binding.opt2.setOnClickListener {
            updateScore(7)
            displayQuestion()}

        binding.opt3.setOnClickListener {
            updateScore(3)
            displayQuestion()}
    }
    private fun displayQuestion() {
        if (currentQuestionIndex < questionList.size) {
            // Display the question and options
            val question = questionList[currentQuestionIndex]
            binding.question.text = question.questionText
            binding.opt1.text = question.option1
            binding.opt2.text = question.option2
            binding.opt3.text = question.option3

            currentQuestionIndex++
        } else {
            // All questions have been answered, show the total score
            saveScoreToFirebase()
            binding.score.text = "Your total score is $totalScore"
        }
    }

    private fun updateScore(score: Int) {
        totalScore += score
    }

    private fun saveScoreToFirebase() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Scores").child(currentUserID)
        databaseRef.setValue(totalScore)
            .addOnSuccessListener {
                Log.d(TAG, "Score saved to Firebase")
            }
            .addOnFailureListener {
                Log.e(TAG, "Error saving score to Firebase", it)
            }
    }

}