package tw.com.ivylin.mykotlinproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

class TrainTime : AppCompatActivity() {

    private lateinit var trainList:ListView
    private var stationId = ""

    //建立一個存放 bike 物件的 array
    var item = ArrayList<TrainItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_time)

        trainList = findViewById(R.id.listView)

        intent?.extras?.let {
            stationId = it.getString("stationid","")
        }
        getTrainTime()

    }

    fun getTrainTime(){
        val url = "https://tdx.transportdata.tw/api/basic/v3/Rail/TRA/StationLiveBoard/Station/"+stationId+"?%24format=JSON"
        val Client = OkHttpClient().newBuilder().build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Accept","application/json")
            .addHeader("Authorization",MainActivity.TOKEN)
            .build()

        //開始連線
        Client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val content = response.body.string()

                var jsonArray: JSONArray = JSONArray()
                var i:Int = 0

                var json : JSONObject = JSONObject(content)
                var station : JSONObject = JSONObject()

                jsonArray = json.getJSONArray("StationLiveBoards")
                var size = jsonArray.length()  //  抓取陣列裡面的數量

                for( i in 0 until size) {
                    json = jsonArray.getJSONObject(i)  // jsonObject

                    station = json.getJSONObject("TrainTypeName") //jsonObject

                    var traStation = station.getString("Zh_tw") //車種

                    var endStation = json.getJSONObject("EndingStationName")
                    var endS= endStation.getString("Zh_tw") //終點站

                    var startTime = json.getString("ScheduleDepartureTime") //開車時間
                    var delay = json.getInt("DelayTime")

                    item.add(TrainItem(traStation,endS,startTime,delay))
                }


                runOnUiThread{
                    //bikeContent.text =msg
                    trainList.adapter = TrainAdapter(item)
                }

            }
        })
    }
}

data class TrainItem (
    val trainType:String,
    val endStation:String,
    val goTime:String,
    val delay:Int
 )