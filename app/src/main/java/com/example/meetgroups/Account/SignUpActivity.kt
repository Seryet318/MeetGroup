package com.example.meetgroups.Account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.meetgroups.DataModels.Account
import com.example.meetgroups.MainActivity
import com.example.meetgroups.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var account: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btn_registration.setOnClickListener(){
            signup_progressBar.visibility = View.VISIBLE
            doRegistration()
        }
    }

    private fun doRegistration(){
        val fName = signup_firstname.text.toString().trim()
        val lName = signup_lastname.text.toString().trim()
        //val username = signup_username.text.toString().trim()
        val email = signup_email.text.toString().trim()
        val psw = signup_psw.text.toString().trim()

        if (signup_firstname.text == null || signup_firstname.text.isEmpty()) {
            signup_firstname.error = "First name missing"
        }
        if (signup_lastname.text == null || signup_lastname.text.isEmpty()) {
            signup_lastname.error = "Last name missing"
        }
        /*if(signup_username.text == null || signup_username.text.isEmpty()){
            signup_username.error = "Username missing"
        }*/
        if (signup_email.text == null || signup_email.text.isEmpty()) {
            signup_email.error = "Email missing"
        }
        if (signup_psw.text == null || signup_psw.text.isEmpty()) {
            signup_psw.error = "Password missing"
        }
        if(signup_conf_psw.text == null || signup_conf_psw.text.isEmpty() || signup_conf_psw.text == signup_psw.text){
            signup_conf_psw.error = "Password is not equals"
            signup_conf_psw.setText("")
        }

        doSignUp(fName, lName, email, psw)
    }

    private fun doSignUp(fName: String, lName: String, email: String, psw: String){
        //account = Account(fName,lName,username,email)
        account = Account(fName,lName,email)

        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(baseContext, p0.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(null, LoginActivity::class.java))
                finish()
            }

            override fun onDataChange(p0: DataSnapshot) {
                auth.createUserWithEmailAndPassword(email, psw)
                    .addOnCompleteListener() { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(
                                baseContext, task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid).setValue(account)
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }
                    }
                }
        })
    }
}