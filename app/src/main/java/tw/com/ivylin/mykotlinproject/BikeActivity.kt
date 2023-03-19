package tw.com.ivylin.mykotlinproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject


class BikeActivity : AppCompatActivity() {

    private lateinit var bikedata:ListView
    private val url = "https://tdx.transportdata.tw/api/basic/v2/Bike/Station/City/Tainan?%24top=300&%24format=JSON" //台南市腳踏車

    //建立連網物件
    private var client = OkHttpClient()
    //建立一個存放 bike 物件的 array
    var item = ArrayList<BikeItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)

        bikedata = findViewById<ListView>(R.id.listView)

        bikedata.setOnItemClickListener {
                parent, view, position, id ->

            val bundle = Bundle() //宣告bundle來打包資料
            bundle.putString("type","bike")
            bundle.putString("station",item[position].bikestation)
            bundle.putDouble("lat",item[position].lat)
            bundle.putDouble("lng",item[position].lng)
            bundle.putInt("rent",item[position].bikerent)
            bundle.putInt("space",item[position].bikespace)

            val it = Intent(this,MapsActivity::class.java)
            it.putExtras(bundle)
            startActivity(it)

//            Toast.makeText(this,item[position].bikestation,Toast.LENGTH_SHORT).show()

        }

        getBike()
    }

    fun history(view:View){
        finish()
    }

    override fun onResume() {
        super.onResume()
        getBike()
    }

    fun getBike(){

        //建立連線
        val request = Request.Builder()
            .url(url)
            .addHeader("Accept","application/json")
            .addHeader("Authorization",MainActivity.TOKEN)
            .build()

        //開始連線
        client.newCall(request).enqueue(object: Callback {

            override fun onFailure(call: Call, e: IOException) {
                //連線失敗時
                Log.e("LCC",e.toString())

            }

            override fun onResponse(call: Call, response: Response) {
                //連線成功並抓取資料回來
                val content = response.body.string()

                var jsonArray: JSONArray = JSONArray(content)
                var i:Int = 0
                var json: JSONObject = JSONObject()
                var size = jsonArray.length()  //  抓取陣列裡面的數量


                for(i in 0 until size){
                    json = jsonArray.getJSONObject(i)

                    var stationId = json.getString("StationID")
                    var station = json.getJSONObject("StationName")
                    var bikeStation = station.getString("Zh_tw")

                    var area: JSONObject = JSONObject()
                    area = json.getJSONObject("StationPosition")
                    var lat = area.getDouble("PositionLat")
                    var lng = area.getDouble("PositionLon")


                    var addr = json.getJSONObject("StationAddress")
                    var address = addr.getString("Zh_tw")

                    item.add(BikeItem(stationId,bikeStation,address,0,0,lat,lng))
                }

                //item 是 ArrayList 是有順序性的存放 object
                // item[0] ==> 台南市腳踏車的第一個內容
                // item[0] ==> 台南市腳踏車的第二個內容

                runOnUiThread{
                    //bikeContent.text =msg
//                    bikedata.adapter = BikeAdapter(R.layout.bikelayout,item)
                    getRealBikeInfo()

                }

            }


        })

    }


    fun getRealBikeInfo(){
        val infoUrl ="https://tdx.transportdata.tw/api/basic/v2/Bike/Availability/City/Tainan?%24top=300&%24format=JSON"
        //建立連線
        val request = Request.Builder()
            .url(infoUrl)
            .addHeader("Accept","application/json")
            .addHeader("Authorization",MainActivity.TOKEN)
            .build()

        //開始連線
        client.newCall(request).enqueue(object: Callback {

            override fun onFailure(call: Call, e: IOException) {
                //連線失敗時
                Log.e("LCC",e.toString())

            }

            override fun onResponse(call: Call, response: Response) {
                //連線成功並抓取資料回來
                val content = response.body.string()

                var jsonArray: JSONArray = JSONArray(content)
                var i:Int = 0
                var json: JSONObject = JSONObject()
                var size = jsonArray.length()  //  抓取陣列裡面的數量


                for(i in 0 until size){
                    json = jsonArray.getJSONObject(i)

                    var stationId = json.getString("StationID")
                    var rent = json.getInt("AvailableRentBikes")
                    var space = json.getInt("AvailableReturnBikes")

                    Log.e("Station",stationId+rent+""+space)

                    //依要找尋的資料 找出對應的索引位置
                    val index = item.indexOfLast {
                        it.bikeId == stationId
                    }
                    item[index].bikerent = rent
                    item[index].bikespace = space
                }

                //item 是 ArrayList 是有順序性的存放 object
                // item[0] ==> 台南市腳踏車的第一個內容
                // item[0] ==> 台南市腳踏車的第二個內容

                runOnUiThread{
                    //bikeContent.text =msg
                    bikedata.adapter = BikeAdapter(R.layout.bikelayout,item)

                }

            }


        })


    }


}

data class BikeItem(
    val bikeId:String,
    val bikestation:String,
    val bikeaddress:String,
    var bikerent:Int,
    var bikespace:Int,
    val lat:Double,
    val lng:Double,


    )