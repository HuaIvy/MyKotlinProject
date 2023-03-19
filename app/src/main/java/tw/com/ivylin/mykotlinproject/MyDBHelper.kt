package tw.com.watsonfan.homework

import android.content.Context

// 要使用SQLite 時，必須嵌入的二個函式庫
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// 自定類別，這個類別繼承：SQLiteOpenHelper
// 確定是否要建立資料表，或是版本更新時，要如何處理
//class MyDBHelper (context:Context,name:String = database,null,null) {
class MyDBHelper (context:Context,name:String = database,factory:SQLiteDatabase.CursorFactory?=null,version:Int = v):
SQLiteOpenHelper(context,name,factory,version){
    companion object{
        private const val database = "accounts" // 資料庫名稱
        private const val v = 1 // 目前的資料庫版本為 1
    }

    //建立資料表用 ，在執行時，android 要自動判斷是否此資料表已存在。若不存在時，會執行　onCreate
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table bookkeeping(bid integer primary key autoincrement,type text,amount int,status text,create_date text)")
        db.execSQL("create table auth(userid integer primary key autoincrement,account varchar(30),password varchar(50) )")
    }

    // 升級資料庫版本時，要刪除舊的資料表，並重新執行 onCreate 方法，來建立新的資料表
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists bookkeeping")
        onCreate(db)
    }


}