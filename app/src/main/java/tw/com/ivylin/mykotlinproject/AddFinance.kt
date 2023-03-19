package tw.com.ivylin.mykotlinproject

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import tw.com.watsonfan.homework.MyDBHelper
import java.util.Calendar

class AddFinance : AppCompatActivity() {

    private lateinit var typeSpinner:Spinner
    private lateinit var statusSpinner:Spinner
    private lateinit var sendBtn:Button
    private lateinit var cancelBtn:Button
    private lateinit var calendar:CalendarView
    private lateinit var amountE:EditText

    val typelist = arrayListOf("食","衣","住","交通","娛樂","其他","獎金")
    val statuslist = arrayListOf("支出","收入")
    var year1:Int = 0
    var month1:Int = 0
    var day1:Int = 0

    private lateinit var dbrw:SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_finance)
        dbrw = MyDBHelper(this).writableDatabase
        findViews()
        val c = Calendar.getInstance()
        year1 = c.get(Calendar.YEAR)
        month1 = c.get(Calendar.MONTH)+1
        day1 = c.get(Calendar.DAY_OF_MONTH)
    }

    fun findViews(){
        typeSpinner = findViewById(R.id.spinner)
        statusSpinner = findViewById(R.id.statusspinner)
        sendBtn = findViewById(R.id.sendfinance)
        cancelBtn = findViewById(R.id.cancelfinance)
        amountE = findViewById(R.id.amountEdt)
        calendar = findViewById(R.id.calendar)

        typeSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,typelist)
        statusSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,statuslist)

        typeSpinner.setSelection(0)
        statusSpinner.setSelection(0)

        calendar.setOnDateChangeListener {
                calendarView, year, month, day ->
            year1 = year
            month1 = month+1
            day1 = day


        }

        sendBtn.setOnClickListener {
            try {
                var cdate = year1.toString() + "-" + month1+ "-"+ day1
                dbrw.execSQL("insert into bookkeeping(type,amount,status,create_date) values(?,?,?,?)",
                    arrayOf(typeSpinner.selectedItem.toString(),amountE.text.toString(),statusSpinner.selectedItem.toString(),cdate)
                )
                Toast.makeText(this,"新增帳務成功!",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,FinanceActivity::class.java))

            }catch (e:Exception){
                Toast.makeText(this,"新增帳務有問題!",Toast.LENGTH_SHORT).show()
            }
        }

        cancelBtn.setOnClickListener {
            finish()
        }

    }
}