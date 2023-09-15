package com.chelshitan.footballetinboard
import Task
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chelshitan.footballetinboard.databinding.ActivityMainBinding
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    // バインディングクラスの変数
    private lateinit var binding: ActivityMainBinding
    var testtext = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            // レイアウトを読み込み
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


// --> GET (https://httpbin.org/get?key=value)
//    Body : (empty)
//    Headers : (2)
//    Accept-Encoding : compress;q=0.5, gzip;q=1.0
//    Device : Android

        // Firestoreをインスタンス化
        val db = Firebase.firestore

        binding.button.setOnClickListener{
            binding.textView.text = testtext
        }

        // ボタンを押したときの処理
        binding.addButton.setOnClickListener {

            // Taskをインスタンス化
            val task = Task(
                title = binding.titleEditText.text.toString(),
            )

            db.collection("tasks")
                .add(task)
                .addOnSuccessListener { documentReference ->
                    //Log.d(ADD_TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    //Log.w(ADD_TAG, "Error adding document", e)
                }
        }

        // RecyclerViewの設定
        val taskAdapter = TaskAdapter()
        binding.recyclerView.adapter = taskAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // アプリ起動時に、保存されているデータを取得する
        db.collection("tasks")
            .get()
            .addOnSuccessListener { tasks ->
                val taskList = ArrayList<Task>()
                tasks.forEach { taskList.add(it.toObject(Task::class.java)) }
                taskAdapter.submitList(taskList)
            }
            .addOnFailureListener { exception ->
                //Log.d(READ_TAG, "Error getting documents: ", exception)
            }

        // データの変更をリアルタイムでアプリに反映する
        db.collection("tasks").addSnapshotListener { tasks, e ->
            if (e != null) {
                //Log.w(READ_TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (tasks != null) {
                val taskList = ArrayList<Task>()
                tasks.forEach { taskList.add(it.toObject(Task::class.java)) }
                taskAdapter.submitList(taskList)
            } else {
                //Log.d(READ_TAG, "Current data: null")
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            APIRequest( )
        }

    }

    //suspend fun APIRequest(){
    //    runBlocking {
    //        val request = Fuel.get(
    //            "https://api.football-data.org/v4/competitions/PL",
    //            parameters = listOf(("X-Auth-Token" to "b5c29124015e48da8293f7fe417e3565"))
    //        ).body
//
// <//-- 200 (https://httpbin.org/get?key=value)
//  //  Body : (empty)
    //        val gson = Gson()
    //        try {
    //            val competition = gson.fromJson(request, MatchdayData::class.java)
    //            testtext = competition.id.toString()
    //            // コードの続き
    //        } catch (e: Exception) {
    //            Log.e("TAG", "エラー発生: ${e.message}", e)
    //        }
    //    }
//
    //}
//
    suspend fun APIRequest(){

/// リクエストURL
        val url = "https://api.football-data.org/v4/competitions/PL"
/// ヘッダー（キー・値のHashMap）
        val headers = hashMapOf(
            "X-Auth-Token" to "b5c29124015e48da8293f7fe417e3565"
        )

        val gson = Gson()

/// GETリクエスト送信！
        Fuel.get(url).header(headers).responseJson {
                request, response, result ->

            when (result) {
                is Result.Failure -> {
                    /// リクエスト失敗・エラー
                    val ex = result.getException()
                    //Log.d(TAG, "Failure : "+ex.toString())
                }
                is Result.Success -> {
                    /// レスポンス正常取得

                    /// JSONObjectに変換
                    val data = result.get().obj()
                    Log.d(TAG, "Responsed JSON : "
                            +data.toString())

                    testtext = data.getString("id")
                }

                else -> {}
            }
        }

    }

}




