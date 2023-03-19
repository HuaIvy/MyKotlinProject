package tw.com.ivylin.mykotlinproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import com.google.android.gms.common.api.Api.Client
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

class TRAActivity : AppCompatActivity() {
    private val traUrl = "https://tdx.transportdata.tw/api/basic/v3/Rail/TRA/Station?%24top=300&%24format=JSON"

    //建立一個存放 TRA 物件的 array
    var item = ArrayList<TRAItem>()

    private lateinit var listView:ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traactivity)
        listView = findViewById(R.id.listView)
        getTRA()

        listView.setOnItemClickListener {
                parent, view, position, id ->

            val bundle = Bundle() //宣告bundle來打包資料
            bundle.putString("type","bike")
            bundle.putString("stationid",item[position].stationId)
            val it = Intent(this,TrainTime::class.java)
            it.putExtras(bundle)
            startActivity(it)

//            Toast.makeText(this,item[position].bikestation,Toast.LENGTH_SHORT).show()

        }
    }

    fun history(view:View){
        finish()
    }

    override fun onResume() {
        super.onResume()
        getTRA()
    }


    fun getTRA(){
        val Client = OkHttpClient().newBuilder().build()
        val request = Request.Builder()
            .url(traUrl)
            .addHeader("Accept","application/json")
            .addHeader("Authorization",MainActivity.TOKEN)
            .build()

        //開始連線
        Client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val content = response.body.string()

                var jsonArray: JSONArray = JSONArray()
                var i:Int = 0

                var json : JSONObject = JSONObject(content)
                var station : JSONObject = JSONObject()

                jsonArray = json.getJSONArray("Stations")
                var size = jsonArray.length()  //  抓取陣列裡面的數量

                for( i in 0 until size) {
                    json = jsonArray.getJSONObject(i)  // jsonObject
                    station = json.getJSONObject("StationName") //jsonObject
                    var stationId = json.getString("StationID")
                    var traStation = station.getString("Zh_tw")

                    var area: JSONObject = JSONObject()
                    area = json.getJSONObject("StationPosition")
                    var lat = area.getDouble("PositionLat")
                    var lng = area.getDouble("PositionLon")

                    item.add(TRAItem(traStation,stationId,lat,lng))
                }


                runOnUiThread{
                    //bikeContent.text =msg
                    listView.adapter = TRAAdapter(item)
                }

            }
        })
    }
}

data class TRAItem(
    val station:String,
    val stationId:String,
    val lat:Double,
    val lng:Double,


    )