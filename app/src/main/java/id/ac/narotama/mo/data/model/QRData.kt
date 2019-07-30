package id.ac.narotama.mo.data.model

import android.util.Base64
import java.io.Serializable
import java.nio.charset.StandardCharsets

data class QRData(
    val nim: String,
    val name: String,
    val group: String
) : Serializable {

    companion object {
        fun parse(rawScannedData: String): QRData {
            val byteData = Base64.decode(rawScannedData, Base64.DEFAULT)
            val strData = String(byteData, StandardCharsets.UTF_8)
            val splits = strData.split(';')

            return QRData(splits[0], splits[1], splits[2])
        }
    }

}