package com.example.meetgroups.Social

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.meetgroups.DataModels.Account
import com.example.meetgroups.DataModels.Post
import com.example.meetgroups.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_new_post.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class NewPostActivity : AppCompatActivity() {
    private lateinit var userRef: DatabaseReference
    private lateinit var postRef: DatabaseReference

    private lateinit var info: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        val tool = findViewById<Toolbar>(R.id.newPost_toolbar)
        setSupportActionBar(tool)

        supportActionBar?.title = getText(R.string.AddPost)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        userRef = FirebaseDatabase.getInstance().getReference("Users")
        postRef = FirebaseDatabase.getInstance().getReference("Posts")

        btn_newPost.setOnClickListener(){
            onClick()
        }
    }

    override fun onStart() {
        super.onStart()

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (ds in p0.children){
                    if(ds.key == Firebase.auth.uid){
                        info = ds.getValue(Account::class.java)!!
                        post_user.text = "${info.firstName} ${info.lastName}"
                        return
                    }
                }
            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun onClick(){
        val bar = findViewById<ProgressBar>(R.id.newPost_progressBar)
        bar.visibility = View.VISIBLE

        val dateServer = SimpleDateFormat("dd-MM-yyyy-HH:mm:ss")
        val dateUser = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val formatServer = dateServer.format(Date())
        val formatUser = dateUser.format(Date())

        val map = HashMap<String, Post>()
        val uid = Firebase.auth.uid

        map["$formatServer-$uid"] =
            Post(
                post_user.text.toString(),
                newPost_description.text.toString(),
                formatUser.toString()
            )

        postRef.updateChildren(map as Map<String, Any>)
            .addOnCompleteListener(){
                if(it.isSuccessful){
                    finish()
                }
            }
    }

}