package com.example.zenQ

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.titclone.databinding.ActivitySignupBinding
import com.example.zenQ.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private var binding: ActivitySignupBinding? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDataBase: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mAuth = FirebaseAuth.getInstance()




        binding?.btnSignup?.setOnClickListener {

            val name = binding?.etUserName?.text.toString()
            val email = binding?.etEmail?.text.toString()
            val password = binding?.etPassword?.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                signup(name, email, password)
            } else {
                Toast.makeText(this@SignupActivity, "Fields can't be empty", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }


    private fun signup(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                addUserToDataBase(name, email, mAuth.currentUser?.uid)

                val intent = Intent(this@SignupActivity, MainActivity::class.java)


                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this@SignupActivity, "Error occurred during sign up", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addUserToDataBase(name: String, email: String, uid: String?) {

        mDataBase = FirebaseDatabase.getInstance().reference

        mDataBase.child("user").child(uid!!).setValue(User(name, email, uid = uid))

    }
}