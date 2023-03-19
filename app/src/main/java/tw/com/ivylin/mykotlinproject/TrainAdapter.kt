package tw.com.ivylin.mykotlinproject

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class TrainAdapter  constructor(private val data: ArrayList<TrainItem>): BaseAdapter() {

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
        val view = View.inflate(parent?.context,R.layout.train,null)

        //我們要從layout 中抓取 id 。上一行是將layout指向view的物件

        var type = view.findViewById<TextView>(R.id.trainType)
        var end = view.findViewById<TextView>(R.id.endStation)
        var startTime =  view.findViewById<TextView>(R.id.startTime)
        var delay = view.findViewById<TextView>(R.id.delayTime)


        type.text = "車種:"+data[position].trainType
        end.text = "終點站:"+data[position].endStation
        startTime.text = "出發時間:"+data[position].goTime
        delay.text = "誤點:"+data[position].delay.toString()

        return view
    }


}