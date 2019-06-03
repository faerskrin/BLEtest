package com.example.bletest.App

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.example.bletest.BleEntity
import com.example.bletest.BleEntityMapper
import org.altbeacon.beacon.*
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconManager


class App: Application(), BeaconConsumer, RangeNotifier {
    private lateinit var beaconManager: BeaconManager
    private val bleManager = BleManager.getInstance()
    private val beacons: HashMap<String, BleEntity> = HashMap()
    private val devices: HashMap<String, BleEntity> = HashMap()
    val devicesLive = MutableLiveData<HashMap<String,BleEntity>>()

    override fun onCreate() {
        super.onCreate()
        devicesLive.value = HashMap()

        bleManager.init(this)
        bleManager.enableBluetooth();

        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"))
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT))
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT))
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT))
        beaconManager.foregroundBetweenScanPeriod = 0
        beaconManager.bind(this)
//        scan()
    }

    override fun onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(this)
        try {
            beaconManager.startRangingBeaconsInRegion(Region("com.example.bletest", null, null, null))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun didRangeBeaconsInRegion(p0: MutableCollection<Beacon>?, p1: Region?) {
        beacons.clear()
        p0?.forEach { beacon ->
            val bleEntity = BleEntityMapper.map(beacon)
            beacons.put(bleEntity.id, bleEntity)
            Toast.makeText(this,"sdfgs",Toast.LENGTH_SHORT).show()
        }
        liveNotify()
    }

    private fun scan(){
        bleManager.scan(object : BleScanCallback(){
            override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                devices.clear()
                scanResultList?.forEach { device ->
                    val bleEntity = BleEntityMapper.map(device)
                    devices.put(bleEntity.id, bleEntity)
                }
                liveNotify()
                scan()
            }
            override fun onScanStarted(success: Boolean) {}
            override fun onScanning(bleDevice: BleDevice?) {}
        })
    }

    private fun liveNotify() {
        val result = HashMap<String, BleEntity>()
        devices.values.forEach { bleEntity -> result.put(bleEntity.id, bleEntity) }
        beacons.values.forEach { bleEntity -> result.put(bleEntity.id, bleEntity) }
        Handler(Looper.getMainLooper()).post{ devicesLive.value = result }
    }
}