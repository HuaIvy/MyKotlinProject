package tw.com.ivylin.mykotlinproject

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class FoodAdapter constructor(private val layout:Int , private val data:ArrayList<FoodItem>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    //override fun getCount(): Int = data.size    單一行要回傳資料時

    override fun getItem(position: Int) = data[position]


    override fun getItemId(position: Int): Long {
        return 0L;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // 這個是客製化畫面layout 時要引用的
        val view = View.inflate(parent?.context,layout,null)
        // 我們要從 layout 中抓取 id 。上一行是將 layout 指向至 view 的物件
        var store = view.findViewById<TextView>(R.id.store)
        var address = view.findViewById<TextView>(R.id.address)
        var tel = view.findViewById<TextView>(R.id.tel)
        var img = view.findViewById<ImageView>(R.id.foodimg)


        store.text = "店名："+data[position].store
        address.text = "地址: "+data[position].address
        tel.text = "電話: "+data[position].tel

        if(data[position].img.length !==0){
            Picasso.with(parent?.context).load(data[position].img).into(img)

        }




        return view
    }


}