package com.example.bletest

import com.clj.fastble.data.BleDevice
import org.altbeacon.beacon.Beacon

class BleEntityMapper{
    companion object {
        fun map(beacon: Beacon): BleEntity{
            return BleEntity(beacon.bluetoothAddress,
                beacon.rssi,
                beacon.id1.toString(),
                "",
                "",
                beacon.txPower,
                beacon.distance)
        }

        fun map(device: BleDevice): BleEntity{
            return BleEntity(device.mac, device.rssi)
        }
    }
}