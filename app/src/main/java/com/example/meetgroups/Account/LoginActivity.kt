package com.example.meetgroups.Account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.meetgroups.DataModels.Account
import com.example.meetgroups.Social.MainActivity
import com.example.meetgroups.R
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val GOOGLE_SIGN = 123

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignIn: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignIn = GoogleSignIn.getClient(this,gso)

        btn_login.setOnClickListener(){
            onLoginClick()
        }
        btn_google.setOnClickListener(){
            onGoogleClick()
        }
        btn_createAccount.setOnClickListener(){
            onSignUp()
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun onLoginClick(){
        if(login_email.text.isEmpty()){
            login_email.error = "Email not valid"
            return
        }

        if(login_psw.text.isEmpty()){
            login_psw.error = "Password not valid"
            return
        }

        doLogin(login_email.text.toString(), login_psw.text.toString())
    }

    private fun doLogin(email: String, psw: String){
        login_progressBar.visibility = View.VISIBLE
        btn_google.isEnabled = false
        btn_login.isEnabled = false
        login_email.isEnabled = false
        login_psw.isEnabled = false
        auth.signInWithEmailAndPassword(email, psw).addOnCompleteListener(this){
            if(it.isSuccessful){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                login_progressBar.visibility = View.INVISIBLE
                btn_google.isEnabled = true
                btn_login.isEnabled = true
                login_email.isEnabled = true
                login_psw.isEnabled = true
                Toast.makeText(baseContext, it.exception!!.message,
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onGoogleClick(){
        login_progressBar.visibility = View.VISIBLE
        btn_google.isEnabled = false
        btn_login.isEnabled = false
        login_email.isEnabled = false
        login_psw.isEnabled = false
        startActivityForResult(mGoogleSignIn.signInIntent, GOOGLE_SIGN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    signInGoogle(account)
                } else {
                    btn_google.isEnabled = true
                    btn_login.isEnabled = true
                    login_email.isEnabled = true
                    login_psw.isEnabled = true
                }
            }catch (e: ApiException){
                btn_google.isEnabled = true
                btn_login.isEnabled = true
                login_email.isEnabled = true
                login_psw.isEnabled = true
            }
        }

    }

    private fun signInGoogle(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider
                .getCredential(account.idToken, null)

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        val account = Account(auth.currentUser!!.displayName?.split(" ")?.get(0)!!, auth.currentUser!!.displayName?.split(" ")?.get(1)!!, auth.currentUser!!.email.toString())


                        var database = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)
                        database.setValue(account).addOnCompleteListener(this){task ->
                            if(task.isSuccessful){

                                startActivity(Intent(this, MainActivity::class.java))
                                finish()

                            }
                        }
                    } else {
                        login_progressBar.visibility = View.INVISIBLE
                        btn_google.isEnabled = true
                        btn_login.isEnabled = true
                        login_email.isEnabled = true
                        login_psw.isEnabled = true
                        Toast.makeText(baseContext, it.exception!!.message,
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun onSignUp(){
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }
}
