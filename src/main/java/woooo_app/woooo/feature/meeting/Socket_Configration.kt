package woooo_app.woooo.feature.meeting

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException

object SocketHandler {
    var mySocketId: String = ""
    private lateinit var mSocket: Socket

    @Synchronized
    fun connectToSocket() {
        try {
            mSocket = IO.socket("https://wmediasoup.watchblock.net/")
            mSocket.connect()
            registerSocketEvents()
        } catch (e: URISyntaxException) {
            Log.d(e.toString(),"Socket Connect Error")
        }
    }

    @Synchronized
    fun mySocketData(): Socket {
        return mSocket
    }

    @Synchronized
    fun disConnectToSocket() {
        mSocket.disconnect()
    }

    @Synchronized
    fun registerSocketEvents() {
        // when user connect receive their id
        mSocket.on("myData") { args ->
            if (args[0] != null) {
                mySocketId = args[0].toString()
                EmitSocketEvent.getRTPCaps(mSocket,args[0].toString())
            }
        }
        // create Device
        mSocket.on("RTPCaps") { args ->
            if (args[0] != null) {
//                RoomClient().mMediasoupDevice = args[0] as Device?
//                Log.d(RoomClient().mMediasoupDevice?.rtpCapabilities.toString(),"casdsdcsdcs")
            }
        }
    }
}

object EmitSocketEvent {
    fun getRTPCaps(mSocket: Socket,socketId: String) {
        val data = JSONObject()
        data.put("id",socketId)
        mSocket.emit("getRTPCaps",data)
    }
}