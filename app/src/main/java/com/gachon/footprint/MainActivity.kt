package com.gachon.footprint

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gachon.footprint.navigation.AddPhotoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    private var listener: ListenerRegistration? = null
    private lateinit var posts: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val actions = LinearLayout(this)
        fun button(label: String, action: () -> Unit) {
            actions.addView(Button(this).apply { text = label; setOnClickListener { action() } },
                LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        }
        button("사진 올리기") { startActivity(Intent(this, AddPhotoActivity::class.java)) }
        button("AR") { startActivity(Intent(this, CameraActivity::class.java)) }
        button("로그아웃") {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        root.addView(actions)
        posts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(24, 16, 24, 16) }
        root.addView(ScrollView(this).apply { addView(posts) })
        setContentView(root)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java)); finish(); return
        }
        listener = FirebaseFirestore.getInstance().collection("images")
            .orderBy("timestamp", Query.Direction.DESCENDING).limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(this, error.localizedMessage ?: "게시물을 불러오지 못했습니다", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                posts.removeAllViews()
                snapshot?.documents?.forEach { post ->
                    posts.addView(TextView(this).apply { text = post.getString("userId") ?: ""; textSize = 16f })
                    val image = ImageView(this).apply {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (280 * resources.displayMetrics.density).toInt())
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        contentDescription = "게시물 사진"
                    }
                    posts.addView(image)
                    Glide.with(this).load(post.getString("imageUrl")).into(image)
                    posts.addView(TextView(this).apply { text = post.getString("explain") ?: ""; setPadding(0, 8, 0, 24) })
                }
                if (snapshot?.isEmpty == true) posts.addView(TextView(this).apply { text = "첫 사진을 올려보세요." })
            }
    }

    override fun onStop() {
        listener?.remove()
        listener = null
        super.onStop()
    }
}
