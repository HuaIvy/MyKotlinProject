package tw.com.ivylin.mykotlinproject

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import tw.com.watsonfan.homework.MyDBHelper

class FinanceActivity : AppCompatActivity() {

    private lateinit var financedata:ListView
    private lateinit var dbrw:SQLiteDatabase
    var item = ArrayList<FinanceItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finance)
        financedata = findViewById(R.id.listView)

        dbrw = MyDBHelper(this).writableDatabase
        Query()

    }

    fun Query(){
        item.clear()
        val sql = "select * from bookkeeping order by bid desc"
        val result = dbrw.rawQuery(sql,null)
        result.moveToFirst()
        for (i in 0 until result.count){
            item.add(FinanceItem(result.getInt(0),result.getString(1),result.getInt(2),result.getString(3),result.getString(4)))
            result.moveToNext()
        }
        financedata.adapter = FinanceAdapter(R.layout.financelist,item)
        result.close()
    }

    override fun onResume() {
        super.onResume()
        Query()
    }

    override fun onDestroy() {
        dbrw.close() //結束時須關閉資料庫
        super.onDestroy()
    }

    fun history(view:View){
        finish()
    }

    fun addFinance(view:View){
        startActivity(Intent(this,AddFinance::class.java))
    }
}
data class FinanceItem(
    val bid:Int,
    val Ftype:String,
    val amount:Int,
    val status:String,
    val create_date:String
)