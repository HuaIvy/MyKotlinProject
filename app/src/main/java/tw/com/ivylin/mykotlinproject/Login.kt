package tw.com.ivylin.mykotlinproject

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import tw.com.watsonfan.homework.MyDBHelper

class Login : AppCompatActivity() {

    private lateinit var account:EditText
    private lateinit var pwd:EditText
    private lateinit var send:Button
    private lateinit var infotxt:TextView

    private lateinit var dbrw:SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dbrw = MyDBHelper(this).writableDatabase

        //新增一個使用者，之後就註解不執行 透過sqlite 登入
//        dbrw.execSQL("insert into auth(account,password) values('ivy','ivy790724')")


        findViews()
    }

    fun findViews(){
        account = findViewById(R.id.useraccount)
        pwd = findViewById(R.id.password)
        send = findViewById(R.id.btnlogin)
        infotxt = findViewById(R.id.errorinfo)

        send.setOnClickListener {
            val acc = account.text.toString()
            val password = pwd.text.toString()

//            if (acc == ("lcc") && password == ("123")){
//                startActivity(Intent(this,MainActivity::class.java))
//                finish()
//            }else{
//                pwd.setText("")
//                infotxt.text = "帳密錯誤 請重新輸入"
//            }

            //使用SQLite登入
            val sql = "select * from auth where account = '"+acc+"' and password='"+password+"' "
            val result = dbrw.rawQuery(sql,null)
            if (result.moveToFirst()){
                result.close()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                result.close()
                pwd.setText("")
                infotxt.text = "帳密錯誤 請重新輸入"
            }
        }
    }


}