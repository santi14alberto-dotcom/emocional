package com.emobot.control
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.util.UUID
class BluetoothRepository {
  var socket: BluetoothSocket? = null
  private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
  fun sendCommand(cmd: String){ try{ socket?.outputStream?.write((cmd+"\n").toByteArray()) }catch(_: Exception){} }
  fun connect(d: BluetoothDevice): Boolean { return try{ socket = d.createRfcommSocketToServiceRecord(uuid); socket?.connect(); true }catch(_: Exception){false} }
}
