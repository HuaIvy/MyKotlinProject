package tw.com.ivylin.mykotlinproject

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import okhttp3.internal.connection.retryTlsHandshake

class FinanceAdapter constructor(private val layout:Int,private val data:ArrayList<FinanceItem>):BaseAdapter() {
    override fun getCount(): Int = data.size

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(parent?.context,layout,null)
        var dateV = view.findViewById<TextView>(R.id.dateview)
        var statusV = view.findViewById<TextView>(R.id.statusview)
        var typeV = view.findViewById<TextView>(R.id.typeview)
        var amountV = view.findViewById<TextView>(R.id.amountview)
        dateV.text = data[position].create_date
        statusV.text = data[position].status
        typeV.text = data[position].Ftype
        amountV.text = ""+data[position].amount
        return view
    }


}