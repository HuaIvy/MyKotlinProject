package tw.com.ivylin.mykotlinproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

class FoodActivity : AppCompatActivity() {

    private val url = "https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx"
    var client = OkHttpClient()
    var item = ArrayList<FoodItem>()
    lateinit var FoodList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        FoodList = findViewById(R.id.listView)

        FoodList.setOnItemClickListener {
                parent, view, position, id ->

            val bundle = Bundle() //宣告bundle來打包資料
            bundle.putString("type","food")
            bundle.putString("store",item[position].store)
            bundle.putString("tel",item[position].tel)
            bundle.putDouble("lat",item[position].lat)
            bundle.putDouble("lng",item[position].lng)


            val it = Intent(this,MapsActivity::class.java)
            it.putExtras(bundle)
            startActivity(it)

//            Toast.makeText(this,item[position].bikestation,Toast.LENGTH_SHORT).show()

        }

        getFood()
    }

    fun history(view: View){
        finish()
    }

    fun getFood(){
        val request = Request.Builder()
            .url(url).build()

        //開始連線
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //連線失敗時
                Log.e("LCC",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                //連線成功並抓取資料回來
                val content = response.body.string()
                var bike = content.toString()


                val jsonArray: JSONArray = JSONArray(bike)
                var i:Int = 0
                var size = jsonArray.length()  //  抓取陣列裡面的數量

                var json : JSONObject = JSONObject()

                for( i in 0 until size){
                    json = jsonArray.getJSONObject(i)  // jsonObject
                    var store = json.getString("Name")
                    var address = json.getString("Address")
                    var tel = json.getString("Tel")
                    var img = json.getString("PicURL")
                    var lat = json.getString("Latitude")
                    var lng = json.getString("Longitude")

                    var doulat:Double
                    var doulng:Double

                    //預防抓下來的資料是空的 經緯度是double 若是空的 轉換時會error
                    if(lat.length==0){
                        doulat = 0.0
                    }else{
                        doulat = lat.toDouble()
                    }

                    if(lng.length == 0){
                        doulng = 0.0
                    }else{
                        doulng = lng.toDouble()
                    }

                    item.add(FoodItem(store,address,tel,img,doulat,doulng))


                }

                runOnUiThread{
                    //bikeContent.text =msg
                    // area = 6 原因是台南區碼

                    FoodList.adapter = FoodAdapter(R.layout.foodlist,item)
                }


            }

        })

    }
}


data class FoodItem(
    val store:String,
    val address: String,
    val tel:String,
    val img:String,
    val lat:Double,
    val lng:Double
)