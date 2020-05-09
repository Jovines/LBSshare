package com.jovines.lbsshare.utils

import kotlin.math.*

/**
 * @author Jovines
 * create 2020-04-30 8:36 下午
 * description:
 */
object LatLonUtil {
    /**
     * 地球赤道半径(km)
     */
    const val EARTH_RADIUS = 6378.137

    /**
     * 地球每度的弧长(km)
     */
    const val EARTH_ARC = 111.199

    /**
     * 转化为弧度(rad)
     */
    private fun rad(d: Double): Double {
        return d * Math.PI / 180.0
    }

    /**
     * 求两经纬度距离
     *
     * @param lon1
     * 第一点的经度
     * @param lat1
     * 第一点的纬度
     * @param lon2
     * 第二点的经度
     * @param lat2
     * 第二点的纬度
     * @return 两点距离，单位km
     */
    fun getDistanceOneKm(
        lon1: Double, lat1: Double, lon2: Double,
        lat2: Double
    ): Double {
        val r1 = rad(lat1)
        val r2 = rad(lon1)
        val a = rad(lat2)
        val b = rad(lon2)
        return (acos(
            cos(r1) * cos(a) * cos(r2 - b)
                    + sin(r1) * sin(a)
        )
                * EARTH_RADIUS)
    }

    fun getDistanceOneM(lon1: Double, lat1: Double, lon2: Double, lat2: Double) =
        getDistanceOneKm(lon1, lat1, lon2, lat2) * 1000

    /**
     * 求两经纬度距离(google maps源码中)
     *
     * @param lon1
     * 第一点的经度
     * @param lat1
     * 第一点的纬度
     * @param lon2
     * 第二点的经度
     * @param lat2
     * 第二点的纬度
     * @return 两点距离，单位km
     */
    fun getDistanceTwoKm(
        lon1: Double, lat1: Double, lon2: Double,
        lat2: Double
    ): Double {
        val radLat1 = rad(lat1)
        val radLat2 = rad(lat2)
        val a = radLat1 - radLat2
        val b = rad(lon1) - rad(lon2)
        var s = 2 * asin(
            sqrt(
                sin(a / 2).pow(2.0)
                        + (cos(radLat1) * cos(radLat2)
                        * sin(b / 2).pow(2.0))
            )
        )
        s *= EARTH_RADIUS
        return s
    }

    fun getDistanceTwoM(
        lon1: Double, lat1: Double, lon2: Double,
        lat2: Double
    ) = getDistanceTwoKm(lon1, lat1, lon2, lat2) * 1000

    /**
     * 求两经纬度距离
     *
     * @param lon1
     * 第一点的经度
     * @param lat1
     * 第一点的纬度
     * @param lon2
     * 第二点的经度
     * @param lat2
     * 第二点的纬度
     * @return 两点距离，单位km
     */
    fun getDistanceThreeKm(
        lon1: Double, lat1: Double,
        lon2: Double, lat2: Double
    ): Double {
        var radLat1 = rad(lat1)
        var radLat2 = rad(lat2)
        var radLon1 = rad(lon1)
        var radLon2 = rad(lon2)
        if (radLat1 < 0) radLat1 = Math.PI / 2 + abs(radLat1) // south
        if (radLat1 > 0) radLat1 = Math.PI / 2 - abs(radLat1) // north
        if (radLon1 < 0) radLon1 = Math.PI * 2 - abs(radLon1) // west
        if (radLat2 < 0) radLat2 = Math.PI / 2 + abs(radLat2) // south
        if (radLat2 > 0) radLat2 = Math.PI / 2 - abs(radLat2) // north
        if (radLon2 < 0) radLon2 = Math.PI * 2 - abs(radLon2) // west
        val x1 = cos(radLon1) * sin(radLat1)
        val y1 = sin(radLon1) * sin(radLat1)
        val z1 = cos(radLat1)
        val x2 = cos(radLon2) * sin(radLat2)
        val y2 = sin(radLon2) * sin(radLat2)
        val z2 = cos(radLat2)
        var d =
            ((x1 - x2).pow(2.0) + (y1 - y2).pow(2.0)
                    + (z1 - z2).pow(2.0))
        // // 余弦定理求夹角
        // double theta = Math.acos((2 - d) / 2);
        d *= EARTH_RADIUS.pow(2.0)
        // //余弦定理求夹角
        val theta = acos(
            (2 * EARTH_RADIUS.pow(2.0) - d)
                    / (2 * EARTH_RADIUS.pow(2.0))
        )
        return theta * EARTH_RADIUS
    }

    /**
     * 求两经纬度方向角
     *
     * @param lon1
     * 第一点的经度
     * @param lat1
     * 第一点的纬度
     * @param lon2
     * 第二点的经度
     * @param lat2
     * 第二点的纬度
     * @return 方位角，角度（单位：°）
     */
    fun getAzimuth(
        lon1: Double, lat1: Double, lon2: Double,
        lat2: Double
    ): Double {
        var mLon1 = lon1
        var mLat1 = lat1
        var mLon2 = lon2
        var mLat2 = lat2
        mLat1 = rad(mLat1)
        mLat2 = rad(mLat2)
        mLon1 = rad(mLon1)
        mLon2 = rad(mLon2)
        var azimuth =
            sin(mLat1) * sin(mLat2) + (cos(mLat1)
                    * cos(mLat2) * cos(mLon2 - mLon1))
        azimuth = sqrt(1 - azimuth * azimuth)
        azimuth = cos(mLat2) * sin(mLon2 - mLon1) / azimuth
        azimuth = asin(azimuth) * 180 / Math.PI
        if (java.lang.Double.isNaN(azimuth)) {
            azimuth = if (mLon1 < mLon2) {
                90.0
            } else {
                270.0
            }
        }
        return azimuth
    }

    /**
     * 已知一点经纬度A，和与另一点B的距离和方位角，求B的经纬度(计算结果有误)
     *
     * @param lon1
     * A的经度
     * @param lat1
     * A的纬度
     * @param distance
     * AB距离（单位：米）
     * @param azimuth
     * AB方位角
     * @return B的经纬度
     */
    fun getOtherPoint(
        lon1: Double, lat1: Double,
        distance: Double, azimuth: Double
    ): String? {
        var mAzimuth = azimuth
        mAzimuth = rad(mAzimuth)
        var ab = distance / EARTH_ARC // AB间弧线长
        ab = rad(ab)
        val lat = asin(
            sin(lat1) * cos(ab) + (cos(lat1)
                    * sin(ab) * cos(mAzimuth))
        )
        val lon = (lon1
                + asin(
            sin(mAzimuth) * sin(ab) / cos(
                lat
            )
        ))
        println("$lon,$lat")
        val a = acos(
            cos(90 - lon1) * cos(ab)
                    + sin(90 - lon1) * sin(ab) * cos(
                mAzimuth
            )
        )
        val c = asin(
            sin(ab) * sin(mAzimuth) / sin(a)
        )
        println("c=$c")
        val lon2 = lon1 + c
        val lat2 = 90 - a
        return "$lon2,$lat2"
    }

    /**
     * 已知一点经纬度A，和与另一点B的距离和方位角，求B的经纬度
     *
     * @param lng1
     * A的经度
     * @param lat1
     * A的纬度
     * @param distance
     * AB距离（单位：米）
     * @param azimuth
     * AB方位角
     * @return B的经纬度
     */
    fun convertDistanceToLogLat(
        lng1: Double, lat1: Double,
        distance: Double, azimuth: Double
    ): String? {
        var mAzimuth = azimuth
        mAzimuth = rad(mAzimuth)
        // 将距离转换成经度的计算公式
        val lon = lng1 + distance * sin(mAzimuth)
        EARTH_ARC * cos(rad(lat1))
        // 将距离转换成纬度的计算公式
        val lat = lat1 + distance * cos(mAzimuth) / EARTH_ARC
        return "$lon,$lat"
    }
}