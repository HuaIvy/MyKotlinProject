package tw.com.ivylin.mykotlinproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import okhttp3.*
import okio.IOException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var tnbike:Button
    private lateinit var ntpbus:Button
    private lateinit var food:Button
    private lateinit var air:Button
    private lateinit var tra:Button
    private lateinit var finance:Button

    //https://tdx.transportdata.tw/api-service/swagger/basic/5fa88b0c-120b-43f1-b188-c379ddb2593d#/TRA/StationApiController_Get_3201
    //authorize 認證
    private var tokenUrl = "https://tdx.transportdata.tw/auth/realms/TDXConnect/protocol/openid-connect/token"
    private var clientId = "ivy418286-147ed8a2-97b7-4730" //之後要換成自己的
    private var clientSecret = "59a348e5-773a-4cb9-88f4-ea3cc2b5c799" //之後要換成自己的

    companion object {
        var TOKEN = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        getTDXToken()
    }

    fun getTDXToken(){
        val client = OkHttpClient().newBuilder().build()

        //使用post 方式傳送值給對方
        //https://github.com/tdxmotc/SampleCode API 使用說明網站
        val requestBody = FormBody.Builder()
            .add("grant_type","client_credentials")
            .add("client_id",clientId)
            .add("client_secret",clientSecret)
            .build()
        val request = Request.Builder()
            .url(tokenUrl)
            .post(requestBody)
            .build()

        val call = client.newCall(request)

        //執行call 連線 使用非同步方式 獲取網站的回應資料
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                var tk = ""
                tk = response.body.string() // 抓取內容後轉換為字串格式

                var json: JSONObject = JSONObject(tk)
//                TDX("Bearer "+json.getString("access_token")) //方式1
                MainActivity.TOKEN = " Bearer "+json.getString("access_token") //方式二 companion object 類似static
                Log.d("Lcc",tk)

            }
        })
    }

    fun findViews(){
        tnbike = findViewById(R.id.btn1)
        ntpbus = findViewById(R.id.btn2)
        tra = findViewById(R.id.btn3)
        air = findViewById(R.id.btn4)
        food = findViewById(R.id.btn5)
        finance = findViewById(R.id.btn6)

        tnbike.setOnClickListener {
            startActivity(Intent(this,BikeActivity::class.java))
        }

        ntpbus.setOnClickListener {
            startActivity(Intent(this,BusActivity::class.java))
        }

        tra.setOnClickListener {
            startActivity(Intent(this,TRAActivity::class.java))
        }

        air.setOnClickListener {
            startActivity(Intent(this,AirActivity::class.java))
        }

        food.setOnClickListener {
            startActivity(Intent(this,FoodActivity::class.java))
        }

        finance.setOnClickListener {
            startActivity(Intent(this,FinanceActivity::class.java))
        }


    }
}

//data class TDX(
//    val token:String
//)