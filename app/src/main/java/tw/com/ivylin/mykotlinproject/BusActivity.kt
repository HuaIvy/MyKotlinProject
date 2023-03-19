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

class BusActivity : AppCompatActivity() {

//    "https://data.ntpc.gov.tw/api/datasets/0ee4e6bf-cee6-4ec8-8fe1-71f544015127/json?size=100"
//    https://data.ntpc.gov.tw/api/datasets/0ee4e6bf-cee6-4ec8-8fe1-71f544015127/json?size=100

//    https://data.ntpc.gov.tw/api/datasets/34b402a8-53d9-483d-9406-24a682c2d6dc/json?size=2000

    private lateinit var busdata: ListView
    private val url = "https://data.ntpc.gov.tw/api/datasets/34b402a8-53d9-483d-9406-24a682c2d6dc/json?size=2000"

    //建立連網物件
    private var client = OkHttpClient()
    //建立一個存放 bike 物件的 array
    var item = ArrayList<BusItem>()
    var allStation = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)

        busdata = findViewById<ListView>(R.id.listView)

        busdata.setOnItemClickListener {
                parent, view, position, id ->

            val bundle = Bundle() //宣告bundle來打包資料
            bundle.putStringArrayList("station",allStation)
            val it = Intent(this,MapsActivity::class.java)
            it.putExtras(bundle)
            startActivity(it)

        }

        getBus()
    }

    fun history(view: View){
        finish()
    }
    fun getBus(){

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
                var bike = content.toString()
                var bikeInfo : JSONArray = JSONArray(bike)
                var i:Int = 0
                var json: JSONObject = JSONObject()
                var msg = ""
                for(i in 0 until bikeInfo.length()){
                    json = bikeInfo.getJSONObject(i)
                    var station = json.getString("nameZh")
                    var address = json.getString("address")
                    var lat = json.getString("latitude").toDouble()
                    var lng = json.getString("longitude").toDouble()

                    //msg += "車站: ${station},\n地址: ${address},\n空位: ${space}\n"

                    item.add(BusItem(station,address,lat,lng))
                }

                //item 是 ArrayList 是有順序性的存放 object
                // item[0] ==> 台南市腳踏車的第一個內容
                // item[0] ==> 台南市腳踏車的第二個內容

                runOnUiThread{
                    //bikeContent.text =msg
                    busdata.adapter = BusAdapter(R.layout.buslayout,item)
                }

            }


        })


    }


}

data class BusItem(

    val station:String,
    val address:String,
    val lat:Double,
    val lng:Double,


    )