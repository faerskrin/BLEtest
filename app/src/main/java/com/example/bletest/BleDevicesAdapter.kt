package com.example.bletest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clj.fastble.data.BleDevice

class BleDevicesAdapter(): RecyclerView.Adapter<BleDevicesAdapter.BleDeviceViewHolder>() {
    private var list: List<BleEntity> = ArrayList()

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(i: Int): Int {
        return if (list[i].uuid == null) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleDeviceViewHolder {
        return BleDeviceViewHolder(LayoutInflater.from(parent.context)
            .inflate( if (viewType == 0) R.layout.rv_device else R.layout.rv_beacon, parent, false))
    }

    override fun onBindViewHolder(holder: BleDeviceViewHolder, i: Int) {
        holder.itemView.findViewById<TextView>(R.id.txt_id).text = list[i].id
        holder.itemView.findViewById<TextView>(R.id.txt_rssi).text = "rssi: " + list[i].rssi
        if (getItemViewType(i) == 1){
            holder.itemView.findViewById<TextView>(R.id.txt_major).text = "major: " + list[i].major
            holder.itemView.findViewById<TextView>(R.id.txt_manor).text = "manor: " + list[i].manor
            holder.itemView.findViewById<TextView>(R.id.txt_power).text = "power: " + list[i].txPower
            holder.itemView.findViewById<TextView>(R.id.txt_distance).text = "distance: " + list[i].distance
        }
    }

    fun updateData(list: List<BleEntity>){
        this.list = list
        notifyDataSetChanged()
    }

    class BleDeviceViewHolder: RecyclerView.ViewHolder{
        constructor(view: View) : super(view)
    }
}