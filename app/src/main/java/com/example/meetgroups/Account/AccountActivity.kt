package com.example.meetgroups.Account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.meetgroups.DataModels.Account
import com.example.meetgroups.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_new_post.*

class AccountActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        auth = Firebase.auth

        val tool = findViewById<Toolbar>(R.id.account_toolbar)
        setSupportActionBar(tool)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        userRef = FirebaseDatabase.getInstance().getReference("Users")

    }

    override fun onStart() {
        super.onStart()
        user_email_txt.text = auth.currentUser?.email
        //fullname_textView.text = auth.currentUser?.displayName

        userRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                var info = p0.getValue(Account::class.java)!!
                fullname_textView.text = "${info.firstName} ${info.lastName}"
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                fullname_textView.text = "${p0.getValue(Account::class.java)?.firstName} ${p0.getValue(Account::class.java)?.lastName}"
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

