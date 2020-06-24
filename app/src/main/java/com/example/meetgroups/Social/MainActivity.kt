package com.example.meetgroups.Social

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.meetgroups.Account.AccountActivity
import com.example.meetgroups.Account.LoginActivity
import com.example.meetgroups.R
import com.example.meetgroups.Social.ShowcaseActivity
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity(),
        Toolbar.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tool = findViewById<Toolbar>(R.id.custom_toolbar)
        setSupportActionBar(tool)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_action_name)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        tool.setOnMenuItemClickListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId){
                R.id.action_account -> startActivity(Intent(this, AccountActivity::class.java))
                R.id.action_showcase -> startActivity(Intent(this, ShowcaseActivity::class.java))
                R.id.action_logout -> {
                    Firebase.auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
        return true
    }
}
