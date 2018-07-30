package com.example.anonymus.likeinsta

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class PostActivity : AppCompatActivity() {
    private val GALLERY_REQUEST = 5
    private lateinit var uri: Uri
    lateinit var  imageButton: ImageButton

    lateinit var  enterName:EditText
    lateinit var  editDesc:EditText
    private var storageReference: StorageReference? = null
    private var databaseReference: DatabaseReference?=null
    private  var database:FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        editDesc=findViewById<EditText>(R.id.editDesc)
        enterName=findViewById<EditText>(R.id.enterName)
        storageReference=FirebaseStorage.getInstance().reference

        databaseReference=FirebaseDatabase.getInstance().reference.child("LikeInsta")

    }



    fun imageButtonClicked(view: View) {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            uri = data.data
            imageButton = findViewById(R.id.imageButton) as ImageButton
            imageButton.setImageURI(uri)
        }

    }

    fun submitButtonClicked(view: View) {
        val titleValue = enterName.text.toString().trim()
        val descValue = editDesc.text.toString().trim()

        if (!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(descValue)) {

            val filePath = storageReference!!.child("PostImage").child(uri.lastPathSegment)
             filePath.putFile(uri).addOnSuccessListener { taskSnapshot ->
                val downloadurl = taskSnapshot.downloadUrl

                Toast.makeText(this@PostActivity, "Upload Complete", Toast.LENGTH_LONG).show()
                val newPost = databaseReference!!.push()
                 newPost.child("title").setValue(titleValue);
                newPost.child("desc").setValue(descValue);
                newPost.child("image").setValue(downloadurl.toString());


            }
        }
    }

}

