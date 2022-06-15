package com.hjam.aada

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import com.hjam.aada.databinding.ActivityMainBinding
import com.hjam.aada.utils.Crc16
import com.hjam.ezbluelib.EzBlue
import java.nio.ByteBuffer
import java.nio.ByteOrder


class MainActivity : AppCompatActivity(), EzBlue.BlueCallback, EzBlue.BlueParser,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var mLblText: TextView
    private lateinit var mBtnSend: Button
    private lateinit var mBtnConnect: Button
    private lateinit var mBtnDisconnect: Button
    private lateinit var navView: NavigationView

    companion object {
        private const val mTag = "AADA_MainActivity"
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

        if (checkPermission(Manifest.permission.BLUETOOTH_CONNECT, BLUETOOTH_PERMISSION_CODE)) {
            startTheApp()
        } else {
            mBtnConnect.isEnabled = false
            mLblText.text = getString(R.string.no_permission)
        }
    }

    private fun setViews() {
        mLblText = binding.root.findViewById(R.id.lbl_text)
        mBtnSend = binding.root.findViewById(R.id.btn_send)
        mBtnConnect = binding.root.findViewById(R.id.btn_connect)
        mBtnDisconnect = binding.root.findViewById(R.id.btn_disconnect)
    }

    private fun bufferProtoTest(long : Long):ByteArray{
        val bb1 = ByteBuffer.allocate(Long.SIZE_BYTES + Float.SIZE_BYTES + Short.SIZE_BYTES)
        bb1.order(ByteOrder.LITTLE_ENDIAN)
        val array = with(bb1){
            putLong(long)
            putFloat(123.456F)
            putShort(4321)
        }.array()
        return array
    }

    private fun startTheApp() {
        mBtnSend.setOnClickListener {
            // a byte array showcase:
            val goodData1 = byteArrayOf(0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39)
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

    // Function to check and request permission.
    private fun checkPermission(permission: String, requestCode: Int): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(permission), requestCode)
            return false
        }
        return true
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
                    inp.map { it.toUByte() }.joinToString()}]")
            try {
                //val bb =  ByteBuffer.wrap(inp)
                //bb.order(ByteOrder.LITTLE_ENDIAN)
                handleData(inp)
            }catch (ex :Exception){
                Log.e(mTag,"bluePackReceived Message "+ex.message.toString())
            }
            val tmp = inp.map { it.toUByte() }.joinToString()
            setStatusText(tmp)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}