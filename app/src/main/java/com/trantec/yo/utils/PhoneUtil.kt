package com.trantec.yo.utils

import com.androidfung.geoip.ServicesManager
import com.androidfung.geoip.model.GeoIpResponseModel
import retrofit2.Call
import retrofit2.Callback
import com.orhanobut.logger.Logger
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and


class PhoneUtil {

    companion object {
        fun getISPInfo(): String {
            val ispName = arrayOf("")

            val ipApiService = ServicesManager.getGeoIpService()
            ipApiService.geoIp.enqueue(object : Callback<GeoIpResponseModel> {

                override fun onResponse(call: Call<GeoIpResponseModel>, response: retrofit2.Response<GeoIpResponseModel>) {
                    val country = response.body()!!.country
                    val city = response.body()!!.city
                    val countryCode = response.body()!!.countryCode
                    val latitude = response.body()!!.latitude
                    val longtidue = response.body()!!.longitude
                    val region = response.body()!!.region
                    val timezone = response.body()!!.timezone
                    val isp = response.body()!!.isp


                    ispName[0] = isp
                }

                override fun onFailure(call: Call<GeoIpResponseModel>, t: Throwable) {
                    t.printStackTrace()
                    ispName[0] = ""
                }
            })

            return ispName[0]
        }

        fun getSha512(s: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-512")
                val digest = md.digest(s.toByteArray())
                val sb = StringBuilder()
                for (i in digest.indices) {
                    sb.append(Integer.toString((digest[i] and 0xff.toByte()) + 0x100, 16).substring(1))
                }
                sb.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                Logger.d("Could not load MessageDigest: SHA-512")
                ""
            }

        }


        fun getSHA512(input: String): String? {

            var toReturn: String? = null
            try {
                val digest = MessageDigest.getInstance("SHA-512")
                digest.reset()
                digest.update(input.toByteArray(charset("utf8")))
                toReturn = String.format("%040x", BigInteger(1, digest.digest()))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return toReturn
        }
    }


}