package com.example.anonymus.likeinsta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var mInstalist:RecyclerView
    private lateinit var mDatabase:DatabaseReference
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        mInstalist=findViewById<RecyclerView>(R.id.insta_list)
        mInstalist.setHasFixedSize(true)
        mInstalist.layoutManager =  LinearLayoutManager(this);
        mDatabase = FirebaseDatabase.getInstance().reference.child("LikeInsta")

        mAuth=FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                val loginIntent = Intent(this, RegisterActivity::class.java)
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(loginIntent)

            }
        }
    }


    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener { mAuthListener!! }
        val FBRA = object : FirebaseRecyclerAdapter<insta, instaViewHolder>(
                insta::class.java,
                R.layout.insta_row,
                instaViewHolder::class.java,
                mDatabase
        ){
            override fun populateViewHolder(viewHolder: instaViewHolder, model: insta, position: Int) {
                viewHolder.setTitle(model.title!!)
                viewHolder.setDesc(model.desc!!)
                viewHolder.setImage(applicationContext, model.image!!)




        }
    }

    mInstalist!!.adapter=FBRA

}


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    class instaViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            val mView = itemView

        }

        fun setTitle(title: String) {
            val post_title = itemView.findViewById<View>(R.id.textTitle) as TextView
            post_title.text = title
        }

        fun setDesc(desc: String) {
            val post_desc = itemView.findViewById<View>(R.id.textDescription) as TextView
            post_desc.text = desc
        }

        fun setImage(ctx: Context, image:String ){
            val post_image = itemView.findViewById<View>(R.id.post_image) as ImageView
            Picasso.with(ctx).load(image).into(post_image)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }
       else if (id == R.id.newIcon) {
           val intent = Intent(this, PostActivity::class.java)
                    startActivity(intent)
        }
        else if(id ==R.id.logout){
            mAuth.signOut()
        }
        return super.onOptionsItemSelected(item)

    }
}
