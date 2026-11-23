package com.gachon.footprint.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gachon.footprint.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.util.UUID

class AddPhotoActivity : AppCompatActivity() {
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        addphoto_image.setOnClickListener { pickImage() }
        addphoto_btn_upload.setOnClickListener { upload() }
        pickImage()
    }

    private fun pickImage() {
        startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            photoUri = data?.data
            addphoto_image.setImageURI(photoUri)
        }
    }

    private fun upload() {
        val uri = photoUri
        val user = FirebaseAuth.getInstance().currentUser
        if (uri == null || user == null) {
            Toast.makeText(this, "사진과 로그인 정보를 확인해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        val ref = FirebaseStorage.getInstance().reference.child("images/${user.uid}/${UUID.randomUUID()}")
        progress_bar.visibility = View.VISIBLE
        addphoto_btn_upload.isEnabled = false
        ref.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) throw task.exception ?: IllegalStateException("Upload failed")
            ref.downloadUrl
        }.addOnSuccessListener { url ->
            val post = hashMapOf("imageUrl" to url.toString(), "uid" to user.uid,
                "userId" to (user.email ?: ""), "explain" to addphoto_edit_explain.text.toString(),
                "timestamp" to System.currentTimeMillis())
            FirebaseFirestore.getInstance().collection("images").add(post)
                .addOnSuccessListener { setResult(Activity.RESULT_OK); finish() }
                .addOnFailureListener { failed(it) }
        }.addOnFailureListener { failed(it) }
    }

    private fun failed(error: Exception) {
        progress_bar.visibility = View.GONE
        addphoto_btn_upload.isEnabled = true
        Toast.makeText(this, error.localizedMessage ?: "업로드에 실패했습니다", Toast.LENGTH_LONG).show()
    }
}
