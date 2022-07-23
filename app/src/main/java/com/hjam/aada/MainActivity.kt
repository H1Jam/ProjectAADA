package com.hjam.aada

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.hjam.aada.comm.DataProtocol
import com.hjam.aada.comm.DataProtocol.handleData
import com.hjam.aada.comm.types.*
import com.hjam.aada.databinding.ActivityMainBinding
import com.hjam.aada.utils.Logger
import com.hjam.ezbluelib.EzBlue
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.nio.ByteBuffer
import java.nio.ByteOrder


class MainActivity : AppCompatActivity(), EzBlue.BlueCallback, EzBlue.BlueParser,
    NavigationView.OnNavigationItemSelectedListener, AADAWriter {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var mLblText: TextView
    private lateinit var mBtnSend: Button
    private lateinit var mBtnConnect: Button
    private lateinit var mBtnDisconnect: Button
    private lateinit var navView: NavigationView
    //private var mapView: MapView? = null
    // private var mapController: IMapController? = null

    companion object {
        private const val mTag = "AADA_MainActivity"
        private const val GPS_STORAGE_PERMISSION_CODE = 100
        private const val BLUETOOTH_PERMISSION_CODE = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navView
        val navController: NavController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        setViews()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                GPS_STORAGE_PERMISSION_CODE
            )
        }

//        if (checkPermission(arrayOf(
//                Manifest.permission.BLUETOOTH_SCAN,
//                Manifest.permission.BLUETOOTH_CONNECT), BLUETOOTH_PERMISSION_CODE)) {
//            startTheApp()
//        } else {
//            //Todo Request Permission
//            mBtnConnect.isEnabled = false
//            mLblText.text = getString(R.string.no_permission)
//        }
    }

    private fun pxToMm(px: Int): Int {
        return (px.toFloat() / TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_MM, 1f, resources.displayMetrics
        )
                ).toInt()
    }

    private fun setViews() {
        mLblText = binding.root.findViewById(R.id.lbl_text)
        mBtnSend = binding.root.findViewById(R.id.btn_send)
        mBtnConnect = binding.root.findViewById(R.id.btn_connect)
        mBtnDisconnect = binding.root.findViewById(R.id.btn_disconnect)
        val canvas1: ConstraintLayout = findViewById(R.id.canvas01);
        ScreenObjects.initScreen(
            this,
            canvas1,
            R.id.canvas01,
            R.id.canvas01,
            resources.displayMetrics,
            this
        )
        Logger.debug(
            mTag, "heightPixels ${resources.displayMetrics.heightPixels} " +
                    "widthPixels ${resources.displayMetrics.widthPixels}"
        )
        Logger.debug(
            mTag, "heightPixels ${pxToMm(resources.displayMetrics.heightPixels)} " +
                    "widthPixels ${pxToMm(resources.displayMetrics.widthPixels)}"
        )
        ScreenObjects.refreshText(86, "Refreshed It!")
        ScreenObjects.refreshText(-10, "Refreshed It! After Set")
        ScreenObjects.refreshText(12, "Refreshed It! After Set!")
        //  ScreenObjects.addButtonToScreen(200, 360, 2, "Button Label ", 15)
        ScreenObjects.addButton(
            AADAButton(44, 400, 1, 0, "vText", 20, Color.WHITE, Color.GREEN),
            this
        )
        //ScreenObjects.addButton(AADAButton(44, 400, 1,2, "vText 2", 20, Color.BLACK, Color.RED),this)
        //ScreenObjects.addButton(AADAButton(44, 400, 1,3, "vText 2", 20, Color.BLACK, Color.RED),this)
        //ScreenObjects.refreshButtonText(255, "Refreshed TexT!")
        //ScreenObjects.addSeekBar(AADASeekBar(10,220,6,0,20,200,300),this)
        //ScreenObjects.addSeekBar(AADASeekBar(10,220,6,2,20,200,300),this)
        //ScreenObjects.addSeekBar(AADASeekBar(10,220,6,3,20,200,300),this)
//        ScreenObjects.addSeekBar(AADASeekBar(10,220,6,1,20,200,300),this)
        ////  ScreenObjects.addSwitch(AADASwitch(60,200,1,0,false,16,Color.WHITE,"MyFirstSwitch"), this)
        // ScreenObjects.addSwitch(AADASwitch(60,200,1,2,false,16,Color.WHITE,"MyFirstSwitch"), this)
        // ScreenObjects.addSwitch(AADASwitch(60,200,1,3,false,16,Color.WHITE,"MyFirstSwitch"), this)
        //ScreenObjects.addSwitch(AADASwitch(60,240,4,0,true,16,Color.RED,"MyFirstSwitch2"), this)
        //ScreenObjects.addSwitch(AADASwitch(60,240,4,0,true,20,Color.YELLOW,"MyFirstSwitch2"), this)
        //ScreenObjects.addSwitch(AADASwitch(0,0,4,1,true,0,0,""), this)
        ScreenObjects.addKnob(AADAKnob(44, 450, 120, -100, 100, 50, "Voltage", 33, 0), this)
        //ScreenObjects.addKnob(AADAKnob(44, 450, 120, -100, 100 ,50, "Voltage", 33,2) ,this)
        //ScreenObjects.addKnob(AADAKnob(44, 450, 120, -100, 100 ,50, "Voltage", 33,3) ,this)
        //ScreenObjects.addKnob(AADAKnob(170, 450, 80, -120, 200, 50, "Current", 34))
        //ScreenObjects.addGauge(AADAGauge(30, 220, 90, 20f, 250f, false, 100f, 120f, 180f, "RPM", 1))
        //ScreenObjects.addGauge(AADAGauge(30, 220, 60, 50f, 250f, false, 100f, 120f, 180f, "RPM", 1))
        //ScreenObjects.addGauge(AADAGauge(0, 0, 0, 100f, 0f, true, 0f, 0f, 0f, "222", 1))
//        ScreenObjects.addMap(
//            AADAMap(
//                10,
//                350,
//                400,
//                300,
//                43.729715839905104f,
//                -79.44888177666463f,
//                11f,
//            0f
//            )
//        )
//        ScreenObjects.addMapMarker(
//            AADAMapMarker(
//                43.729725839905104f,
//                -79.44888177666463f,
//                45f,
//                1,
//                0,
//                1
//            )
//        )

    }

    private fun startMap() {
        Logger.debug(mTag, "startMap()")
        val ctx: Context = applicationContext
        val shr = this.getSharedPreferences("map", MODE_PRIVATE)
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID;
        ScreenObjects.mapPermissions = true
    }

    private fun bufferProtoTest(long: Long): ByteArray {
        val bb1 = ByteBuffer.allocate(Long.SIZE_BYTES + Float.SIZE_BYTES + Short.SIZE_BYTES)
        bb1.order(ByteOrder.LITTLE_ENDIAN)
        val array = with(bb1) {
            putLong(long)
            putFloat(123.456F)
            putShort(4321)
        }.array()
        return array
    }

    private fun startTheApp() {
        mBtnSend.setOnClickListener {
            // a byte array showcase:
            EzBlue.write(DataProtocol.prepareFrame(bufferProtoTest(System.currentTimeMillis())))
            //EzBlue.write(DataProtocol.prepareFrame(goodData1))
            // or just use the above line for single byte transfer:
            //EzBlue.write(counterd)
        }
        mBtnConnect.isEnabled = true
        mBtnConnect.setOnClickListener {
            showDevList()
        }
        mBtnDisconnect.setOnClickListener {
            EzBlue.stop()
        }


        //canvas1.requestLayout()
//        try {
//            ScreenObjects.initScreen(canvas1,R.id.canvas01,R.id.canvas01)
//            ScreenObjects.addTextToScreen(100,200,10,50,1,"First Test Label")
//        }catch (ex: Exception){
//            Logger.error(mTag, ex.message.toString())
//        }
    }

    private fun showDevList() {
        val mmDevList = EzBlue.getBondedDevices()
        showDeviceListDialog(mmDevList.toTypedArray())
    }

    @SuppressLint("MissingPermission")
    private fun showDeviceListDialog(devices: Array<BluetoothDevice>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an animal")
        val mmListData: Array<String> = devices.map { it.name }.toTypedArray()
        builder.setItems(mmListData) { _, which ->
            connectToDevFromDialog(devices[which])
        }
        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevFromDialog(dev: BluetoothDevice) {
        Log.e(mTag, dev.name + ":" + dev.address)
        connectToDev(dev)
        setStatusText("Connecting...")
        mBtnConnect.isEnabled = false
    }

    private fun setStatusText(str: String) {
        mLblText.text = str
    }

    private fun connectToDev(dev: BluetoothDevice) {
        // Use the first method to get each bytes in the `dataRec` callback in the main thread.
        // EzBlue.init(dev, true, this)
        // Use the second method to apply a custom parser. The custom parser runs on Ezblue thread
        // thus you should not update UI in that callback.
        EzBlue.init(dev, true, this, this)
        EzBlue.start()
    }


    private var requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(mTag, "Permission in granted!")
                startTheApp()
            } else {
                mBtnConnect.isEnabled = false
                mLblText.text = getString(R.string.no_permission)
                Log.e(mTag, "Permission in NOT granted!")
            }
        }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.all {
                    Log.d(mTag, "${it.key} = ${it.value}")
                    it.value == true
                }) {
                startTheApp()
                startMap()
            } else {
                mBtnConnect.isEnabled = false
                mLblText.text = getString(R.string.no_permission)
                Log.e(mTag, "Permission in NOT granted!")
            }
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BLUETOOTH_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(mTag, "PERMISSION GRANTED")
                startTheApp()
            } else {
                Log.e(mTag, "Permission Denied!")
            }
        }
        if (requestCode == GPS_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMap()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    var mCounter: Int = 0
    var mInpCounter: Int = 0
    override fun dataRec(inp: Int) {
        mInpCounter++
        if ((mInpCounter % 500) == 0) {
            "$mInpCounter".also { mLblText.text = it }
        }
    }

    override fun connected() {
        mInpCounter = 0
        Log.d(mTag, "connected!")
        setStatusText("Connected!")
        mBtnDisconnect.isEnabled = true
        mBtnSend.isEnabled = true
    }

    override fun disconnected() {
        Log.d(mTag, "connectionFailed!")
        setStatusText("Disconnected!")
        mBtnConnect.isEnabled = true
        mBtnDisconnect.isEnabled = false
        mBtnSend.isEnabled = false
    }


    override fun onResume() {
        super.onResume()
        ScreenObjects.mapViewResume()
    }

    override fun onPause() {
        super.onPause()
        ScreenObjects.mapViewPause()
    }

    override fun onDestroy() {
        EzBlue.stop()
        super.onDestroy()
    }

    /**
     * A byte receive callback. When a byte appears in the stream this method will be invoked.
     * The method runs on Bluetooth thread. Do not update UI here!
     * @param inp: an Int from input stream.
     * @return ArrayList<Byte> if the packed was parsed otherwise returns null.
     */
    override fun parseIt(inp: Int): ByteArray? {
        return DataProtocol.parseIt(inp)
    }

    /**
     * Packet receive callback. When your packed is ready this method will be invoked.
     * The method runs on the UI thread. It is safe to update UI here.
     * @param inp: an ArrayList containing the packed body parsed in the parseIt stage.
     * @return void
     */
    override fun bluePackReceived(inp: ByteArray?) {
        if (inp != null) {
            Log.d(
                mTag,
                "BluePackReceived size=${inp.size} Data=[${
                    inp.map { it.toUByte() }.joinToString()
                }]"
            )
            try {
                //val bb =  ByteBuffer.wrap(inp)
                //bb.order(ByteOrder.LITTLE_ENDIAN)
                handleData(inp, this)
            } catch (ex: Exception) {
                Log.e(mTag, "bluePackReceived Message " + ex.message.toString())
            }
            val tmp = inp.map { it.toUByte() }.joinToString()
            setStatusText(tmp)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun write(buffer: ByteArray?) {
        Logger.debug(
            mTag,
            "writeData From Listener: ${buffer?.map { it.toUByte() }?.joinToString()}"
        )
        EzBlue.write(buffer)
    }
}