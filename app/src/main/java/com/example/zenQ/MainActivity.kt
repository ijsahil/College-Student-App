package com.example.zenQ

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.titclone.R
import com.example.titclone.databinding.ActivityMainBinding
import com.example.zenQ.model.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var navView: NavigationView
    private lateinit var navDrawerLayout: DrawerLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    private var user: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        navView = findViewById(R.id.navView)
        navDrawerLayout = findViewById(R.id.my_drawer_layout)
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        mDatabaseReference.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapShot in snapshot.children) {
                    val currentUser = postSnapShot.getValue(User::class.java)
                    if (currentUser?.uid.equals(mAuth.currentUser?.uid)) {
                        user = currentUser
                    }
                }
                binding?.tvName?.text = user?.name.toString()
                binding?.tvEmail?.text = user?.email.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })




        binding?.rbFees?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding?.svAcademic?.visibility = View.GONE
                binding?.svFees?.visibility = View.VISIBLE
            } else {
                binding?.svFees?.visibility = View.GONE
                binding?.svAcademic?.visibility = View.VISIBLE
            }
        }

        binding?.ivNavigationMenu?.setOnClickListener {
            navDrawerLayout.openDrawer(GravityCompat.END)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    mAuth.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            navDrawerLayout.closeDrawers()
            true
        }


        // Adding the chat card view clickable
        binding?.cvChat?.setOnClickListener {
            val intent = Intent(this@MainActivity, ChatActivity::class.java)
            startActivity(intent)
        }


    }

}
