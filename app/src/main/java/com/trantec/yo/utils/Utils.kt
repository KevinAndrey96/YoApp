package com.trantec.yo.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class Utils {


    fun convertBitmapToBytes(var0: Bitmap): ByteArray {
        val var1: Any? = null
        var var2: ByteArrayOutputStream? = ByteArrayOutputStream()
        var0.compress(Bitmap.CompressFormat.JPEG, 100, var2)
        val var5 = var2!!.toByteArray()

        try {
            var2.close()
            var2 = null
        } catch (var4: Exception) {
            var4.printStackTrace()
        }

        return var5
    }
}