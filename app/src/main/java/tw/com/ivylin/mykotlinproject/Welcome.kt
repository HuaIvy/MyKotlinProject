package tw.com.ivylin.mykotlinproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message

class Welcome : AppCompatActivity() {

    private lateinit var myThread:Thread
    private lateinit var handler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initPage()
    }

    fun initPage(){
        myThread = object : Thread(){
           override fun run(){
               super.run()
               handler.sendEmptyMessageDelayed(1,2000)

            }
        }
        //初始化 handler
        handler = object : Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                //通常會透過 msg 來判斷要處理的事情
                var it = Intent()
                it.setClass(this@Welcome,Login::class.java)
                startActivity(it)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(!myThread.isAlive)
            myThread.start()
    }
}