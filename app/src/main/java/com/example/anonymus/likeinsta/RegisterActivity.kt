package com.example.anonymus.likeinsta

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.EditText

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Anonymus on 02.05.2018.
 */

class RegisterActivity : AppCompatActivity() {
    private var nameField: EditText? = null
    private var emailField: EditText? = null
    private var passField: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameField = findViewById<View>(R.id.nameField) as EditText
        passField = findViewById<View>(R.id.passField) as EditText
        emailField = findViewById<View>(R.id.emailField) as EditText
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference.child("Users")
    }

    fun registerButtonClicked(view: View) {

        val name = nameField!!.text.toString().trim { it <= ' ' }
        val email = emailField!!.text.toString().trim { it <= ' ' }
        val pass = passField!!.text.toString().trim { it <= ' ' }

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            mAuth!!.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user_id = mAuth!!.currentUser!!.uid
                    val current_user_db = mDatabase!!.child(user_id)
                    current_user_db.child("Name").setValue(name)
                    current_user_db.child("Image").setValue("default")

                    val mainIntent = Intent(this@RegisterActivity, MainActivity::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(mainIntent)

                }
            }
        }
    }
}
