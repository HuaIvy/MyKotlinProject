package tw.com.ivylin.mykotlinproject

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


//area 分辨是哪個區域的bike
class BikeAdapter constructor(private val layout:Int,private val data: ArrayList<BikeItem>):BaseAdapter() {
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
        //這個是客製化畫面 layout 時要引用的
        val view = View.inflate(parent?.context,layout,null)
        //我們要從layout 中抓取 id 。上一行是將layout指向view的物件
        var station = view.findViewById<TextView>(R.id.station)
        var address = view.findViewById<TextView>(R.id.address)
        var bikerent = view.findViewById<TextView>(R.id.bikerent)
        var bikespace = view.findViewById<TextView>(R.id.bikespace)

        var bikeimg = view.findViewById<ImageView>(R.id.bikeimg)

        station.text = "站名:"+data[position].bikestation
        address.text = "地址:"+data[position].bikeaddress
        bikerent.text = "數量:"+data[position].bikerent.toString() // 內容是 Int 所以要轉成String
        bikespace.text = "空位:"+data[position].bikespace.toString()


            bikeimg.setImageResource(R.mipmap.bike)


        return view
    }


}