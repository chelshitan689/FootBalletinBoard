package com.chelshitan.footballetinboard
import Task
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chelshitan.footballetinboard.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    // バインディングクラスの変数
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // レイアウトを読み込み
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firestoreをインスタンス化
        val db = Firebase.firestore

        // Taskをインスタンス化
        val task = Task(
            "test","test",)
        db.collection("test")
            .add(task)
            .addOnSuccessListener { documentReference ->
                //Log.d(ADD_TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                //Log.w(ADD_TAG, "Error adding document", e)
            }
    }
}