package tw.com.ivylin.mykotlinproject

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import tw.com.ivylin.mykotlinproject.AirItem

class AirAdapter constructor(private val data: ArrayList<AirItem>):
    BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

//    override fun getCount(): Int = data.size

//    override fun getItem(position: Int) = data[position]

    override fun getItem(position: Int): Any {
        return data[position]
    }


    override fun getItemId(position: Int): Long {
        return 0L;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View

        //這個是客製化畫面 layout 時要引用的
        if(convertView == null){
            view = View.inflate(parent?.context,R.layout.airlayout,null)
        }else{
            view = convertView
        }




        //我們要從layout 中抓取 id 。上一行是將layout指向view的物件
        var airarea = view.findViewById<TextView>(R.id.airarea)
        var status = view.findViewById<TextView>(R.id.airstatus)
        var aqi = view.findViewById<TextView>(R.id.airaqi)

        if(data[position].aqi > 50){
//            aqi.setTextColor(Color.YELLOW)
            view.setBackgroundColor(Color.YELLOW)
        }else{
//            aqi.setTextColor(Color.BLACK)
            view.setBackgroundColor(Color.GREEN)
        }


        airarea.text = "地區:"+data[position].area
        status.text = "狀態:"+data[position].status
        aqi.text = "AQI:"+data[position].aqi // 內容是 Int 所以要轉成String


        return view
    }


}