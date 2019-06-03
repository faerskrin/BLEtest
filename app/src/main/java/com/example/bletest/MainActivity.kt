package com.example.bletest

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.example.bletest.App.App
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    companion object {
        val coarseLocationPermission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        val COARSE_LOCATION = 1
    }

    private lateinit var adapter: BleDevicesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val app = application as App
        app.devicesLive.observe(this, devicesObserver)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = BleDevicesAdapter()
        rv.adapter = adapter

        ActivityCompat.requestPermissions(this, coarseLocationPermission, COARSE_LOCATION)
    }

    private val devicesObserver = Observer<HashMap<String, BleEntity>>{ map ->
        adapter.updateData(map.values.sortedBy { -it.rssi })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == COARSE_LOCATION) {

        }
    }

    override fun onPermissionsDenied(requestCode: Int, permList: List<String>) {
        if (requestCode == COARSE_LOCATION) {
            MaterialDialog(this)
                .title(text = "Ошибка")
                .message(text = "Необходимо дать разрешение")
                .positiveButton(text = "Хорошо")
                .onDismiss{
                    ActivityCompat.requestPermissions(this, coarseLocationPermission, COARSE_LOCATION)
                }.show()
        }
    }
}
