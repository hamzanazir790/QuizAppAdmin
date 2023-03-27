package com.example.quizappadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizappadmin.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val database = FirebaseDatabase.getInstance().reference
    val questionsRef = database.child("Best Friend Test")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {

            val question = Question(
                binding.question.text.toString(),
                binding.opt1.text.toString(),
                binding.opt2.text.toString(),
                binding.opt3.text.toString()
            )
            questionsRef.push().setValue(question)

            binding.question.setText("")
            binding.opt1.setText("")
            binding.opt2.setText("")
            binding.opt3.setText("")
        }

        binding.showData.setOnClickListener {

            val intent = Intent(this@MainActivity,Show_Question::class.java)
            startActivity(intent)
        }


    }
}