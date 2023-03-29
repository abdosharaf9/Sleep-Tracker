package com.abdosharaf.sleeptracker.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun getTime(time: Long): String {
    return SimpleDateFormat("EEEE dd-MMM-yyyy 'at' HH:mm a").format(time).toString()
}

fun getTotalTime(startTime: Long, endTime: Long): String {

    val days = (endTime.minus(startTime)) / 1000 / 60 / 60 / 24
    val hours = (endTime.minus(startTime)) / 1000 / 60 / 60
    val minutes = (endTime.minus(startTime)) / 1000 / 60

    var total = ""

    if(days > 0) {
        total += "$days Days "
    }
    if(hours > 0) {
        total += "$hours Hours "
    }
    total += "$minutes Minutes"

    /*Log.d("getTotalTime", "days = $days")
    Log.d("getTotalTime", "hours = $hours")
    Log.d("getTotalTime", "minutes = $minutes")
    Log.d("getTotalTime", "total = $total")
    Log.d("getTotalTime", "-------------------------------")*/

    return total
}