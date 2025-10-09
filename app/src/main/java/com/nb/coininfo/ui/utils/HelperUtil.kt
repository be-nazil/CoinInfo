package com.nb.coininfo.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nb.coininfo.data.models.CoinEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String?.orNA() = this ?: "NA"
fun Int?.orZero() = this ?: 0

fun getChartDateRange(daysAgo: Long): Pair<String, String> {
    // Define the desired date format
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Get today's date to use as the end date
    val endDate = LocalDate.now()

    // Calculate the start date by subtracting the specified number of days
    val startDate = endDate.minusDays(daysAgo)

    // Format and return the date strings
    return Pair(startDate.format(formatter), endDate.format(formatter))
}


inline fun <reified T> convertDataClassToJsonString(dataClass: T): String =
    Gson().toJson(dataClass)

inline fun <reified T> convertJsonStringToDataClass(jsonString: String): T =
    Gson().fromJson(jsonString, T::class.java)

fun openUrlSafe(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}
