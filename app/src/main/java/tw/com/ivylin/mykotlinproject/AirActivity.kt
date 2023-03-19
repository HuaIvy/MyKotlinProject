package tw.com.ivylin.mykotlinproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject


class AirActivity : AppCompatActivity() {

    val url = "https://data.epa.gov.tw/api/v2/aqx_p_432?api_key=e8dd42e6-9b8b-43f8-991e-b3dee723a52d&limit=1000&sort=ImportDate%20desc&format=JSON" //空氣品質AQI

    //建立連網物件
    var client = OkHttpClient()
    //建立一個存放 bike 物件的 array
    var item = ArrayList<AirItem>()
    lateinit var airList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_air)
        airList = findViewById<ListView>(R.id.listView)

//        airList.setOnItemClickListener {
//                parent, view, position, id -> Toast.makeText(this,item[position].area, Toast.LENGTH_SHORT).show()
//
//        }


        getAir()
    }

    fun history(view: View){
        finish()
    }

    fun  getAir(){

        //建立連線
        val request = Request.Builder()
            .url(url).build()

        //開始連線
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                //連線失敗時
                Log.e("LCC",e.toString())

            }

            override fun onResponse(call: Call, response: Response) {
                //連線成功並抓取資料回來
                val content = response.body.string()
                //Log.d("LCC",content+"")
                var air = content.toString()

                //先抓取 JSONObject
                val airObj : JSONObject? = JSONObject(air)
                var airContent = airObj?.getString("records")
                //airContent 已經是 JSONArray 基本型態 所以要轉為 JSONArray

                var jsonArray : JSONArray = JSONArray(airContent)
                var i:Int = 0
                var json: JSONObject = JSONObject()
                var msg = ""
                for(i in 0 until jsonArray.length()){
                    json = jsonArray.getJSONObject(i)
                    var area = json.getString("sitename")
                    var status = json.getString("status")

                    var aqiStr = json.getString("aqi")
                    var aqi:Int
                    if(aqiStr == "")
                        aqi = 0
                    else
                        aqi = Integer.parseInt(aqiStr)

//                    var aqi = Integer.parseInt(json.getString("aqi"))

                    var lat = json.getString("latitude").toDouble()
                    var lng = json.getString("longitude").toDouble()

                    item.add(AirItem(area,status,aqi,lat,lng))
                }

                //item 是 ArrayList 是有順序性的存放 object
                // item[0] ==> 台南市腳踏車的第一個內容
                // item[0] ==> 台南市腳踏車的第二個內容

                runOnUiThread{
                    //bikeContent.text =msg
                    airList.adapter = AirAdapter(item)
                }



            }


        })

    }
}

data class AirItem(

    val area:String,
    val status:String,
    val aqi:Int,
    val lat:Double,
    val lng:Double

)
