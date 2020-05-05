package com.jovines.lbsshare.utils

import kotlin.math.*

/**
 * @author Jovines
 * create 2020-04-30 8:36 下午
 * description:
 */
object LatLonUtil {
    private const val PI = 3.14159265
    private const val EARTH_RADIUS = 6378137.0
    private const val RAD = Math.PI / 180.0

    /// <summary>
    /// 根据提供的经度和纬度、以及半径，取得此半径内的最大最小经纬度
    /// </summary>
    /// <param name="lat">纬度</param>
    /// <param name="lon">经度</param>
    /// <param name="raidus">半径(米)</param>
    /// <returns></returns>
    fun getRange(lat: Double, lon: Double, range: Int): DoubleArray {
        val degree = 24901 * 1609 / 360.0
        val rMile = range.toDouble()
        val dpmLat = 1 / degree
        val radiusLat = dpmLat * rMile
        val minLat = lat - radiusLat
        val maxLat = lat + radiusLat
        val mpdLng = degree * cos(lat * (PI / 180))
        val dpmLng = 1 / mpdLng
        val radiusLng = dpmLng * rMile
        val minLon = lon - radiusLng
        val maxLon = lon + radiusLng
        return doubleArrayOf(minLat, minLon, maxLat, maxLon)
    }

    /// <summary>
    /// 根据提供的两个经纬度计算距离(米)
    /// </summary>
    /// <param name="lon1">经度1</param>
    /// <param name="lat1">纬度1</param>
    /// <param name="lon2">经度2</param>
    /// <param name="lat2">纬度2</param>
    /// <returns></returns>
    fun getDistance(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Double {
        val radLat1 = lat1 * RAD
        val radLat2 = lat2 * RAD
        val a = radLat1 - radLat2
        val b = (lon1 - lon2) * RAD
        var s = 2 * asin(
            sqrt(
                sin(a / 2).pow(2.0) +
                        cos(radLat1) * cos(radLat2) * sin(b / 2).pow(2.0)
            )
        )
        s *= EARTH_RADIUS
        s = (s * 10000).roundToInt() / 10000.toDouble()
        return s
    }

}