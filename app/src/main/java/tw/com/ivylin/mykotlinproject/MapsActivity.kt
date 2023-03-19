package tw.com.ivylin.mykotlinproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import tw.com.ivylin.mykotlinproject.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    //申請 API 憑證位置 https://console.cloud.google.com/apis/credentials?project=hismaxmap

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var station:String=""
    private var lat:Double = 0.0
    private var lng:Double = 0.0
    private var rent:Int = 0
    private var space:Int = 0

    private var type:String = ""
    private var store:String = ""
    private var tel:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        intent?.extras?.let {
            type = it.getString("type","")
            if(type.equals("bike")){
                station = it.getString("station","")
                lat = it.getDouble("lat",0.0)
                lng = it.getDouble("lng",0.0)
                rent = it.getInt("rent",0)
                space = it.getInt("space",0)
            }else if(type.equals("food")){
                store = it.getString("store","")
                tel = it.getString("tel","")
                lat = it.getDouble("lat",0.0)
                lng = it.getDouble("lng",0.0)

            }else{
                station = it.getStringArrayList("station").toString()
            }

            Log.e("Map",station)


        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val area = LatLng(lat, lng)
        if(type.equals("bike")){
            mMap.addMarker(MarkerOptions().position(area).title(station).snippet("可借:"+rent+",可停:"+space))
        }else if(type.equals("food")){
            mMap.addMarker(MarkerOptions().position(area).title(station).snippet("電話:"+tel))
        }else{
            mMap.addMarker(MarkerOptions().position(area).title(station))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(area,16F))
    }
}