package com.chelshitan.footballetinboard

import android.os.AsyncTask
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import com.google.gson.Gson

var testtext = ""
class MyTask : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        // Fuelの初期化
        FuelManager.instance.basePath = "https://api.football-data.org/v4"
        FuelManager.instance.baseHeaders = mapOf("X-Auth-Token" to "b5c29124015e48da8293f7fe417e3565")

        val gson = Gson()
        var json = ""
        // API呼び出し
        val endpoint = "/competitions/PL"
        val (request, response, result) = Fuel.request(Method.GET, endpoint)
            .responseString()

        // レスポンスの処理
        result.fold(
            { data ->
                //println(data)
                json = data
                //val competition = gson.fromJson(data, MyTask::class.java)
                //binding.textView.text = competition.toString()
            },
            { error ->
                println("リクエストエラー: $error")
            }
        )

        try {

            val competition = gson.fromJson(json, MatchdayData::class.java)
            testtext = competition.id.toString()
            // コードの続き
        } catch (e: Exception) {
            Log.e("TAG", "エラー発生: ${e.message}", e)
        }


        return "dummy"
    }
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // ネットワークリクエストの結果をUIに反映するなどの処理を行う
        println(result)
    }
}


