package com.example.meetgroups.Social

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetgroups.Adapters.PostHolder
import com.example.meetgroups.DataModels.Post
import com.example.meetgroups.R
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class ShowcaseActivity : AppCompatActivity(),
        Toolbar.OnMenuItemClickListener {

    private val adapter: FirebaseRecyclerAdapter<Post, PostHolder>
    private lateinit var dataList: RecyclerView

    init {
        val options = FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(
                        FirebaseDatabase.getInstance().getReference("Posts"),
                        Post::class.java
                )
                .setLifecycleOwner(this)
                .build()

        adapter = object : FirebaseRecyclerAdapter<Post, PostHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
                return PostHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.post_card_item, parent, false))
            }

            override fun onBindViewHolder(holder: PostHolder, position: Int, model: Post) {
                holder.bind(model)
            }

            override fun onChildChanged(
                    type: ChangeEventType,
                    snapshot: DataSnapshot,
                    newIndex: Int,
                    oldIndex: Int
            ) {
                super.onChildChanged(type, snapshot, newIndex, oldIndex)
                dataList.scrollToPosition(newIndex)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showcase)

        val tool = findViewById<Toolbar>(R.id.showcase_toolbar)
        setSupportActionBar(tool)

        supportActionBar?.title = getText(R.string.Showcase)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        dataList = findViewById(R.id.showcase_recyclerView)

        dataList.adapter = adapter
        val layout = LinearLayoutManager(this)
        layout.reverseLayout = true
        dataList.layoutManager = layout
        dataList.setHasFixedSize(true)

        tool.setOnMenuItemClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.showcase_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.action_addPost -> startActivity(Intent(this, NewPostActivity::class.java))
            }
        }
        return true
    }


}