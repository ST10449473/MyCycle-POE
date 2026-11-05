package com.example.mycycle.database

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class PeriodEntity(
    val userId: Int,
    val startDate: String,
    val endDate: String,
    val symptoms: String = "",
    val mood: String = "",
    val notes: String = ""
)

object PeriodLocalStorage {

    private const val PREF_NAME = "MyCycleOfflineData"
    private const val KEY_PERIODS = "periods_"
    private const val KEY_PENDING = "pending_periods_"

    fun savePeriods(
        context: Context,
        userId: Int,
        periods: List<PeriodEntity>
    ) {
        saveList(context, "${KEY_PERIODS}$userId", periods)
    }

    fun getPeriods(
        context: Context,
        userId: Int
    ): List<PeriodEntity> {
        return getList(context, "${KEY_PERIODS}$userId")
    }

    fun saveLocalPeriod(context: Context, period: PeriodEntity) {
        val periods = getPeriods(context, period.userId).toMutableList()
        periods.add(period)
        savePeriods(context, period.userId, periods)
    }

    fun getPendingPeriods(
        context: Context,
        userId: Int
    ): List<PeriodEntity> {
        return getList(context, "${KEY_PENDING}$userId")
    }

    fun addPendingPeriod(context: Context, period: PeriodEntity) {
        val pending = getPendingPeriods(context, period.userId).toMutableList()
        pending.add(period)
        saveList(context, "${KEY_PENDING}${period.userId}", pending)
    }

    fun removePendingPeriod(context: Context, period: PeriodEntity) {
        val pending = getPendingPeriods(context, period.userId).toMutableList()
        // We match by dates and userId as a simple identifier for removal
        pending.removeAll { 
            it.startDate == period.startDate && 
            it.endDate == period.endDate &&
            it.userId == period.userId
        }
        saveList(context, "${KEY_PENDING}${period.userId}", pending)
    }

    private fun saveList(context: Context, key: String, periods: List<PeriodEntity>) {
        val jsonArray = JSONArray()
        periods.forEach { period ->
            val jsonObject = JSONObject().apply {
                put("userId", period.userId)
                put("startDate", period.startDate)
                put("endDate", period.endDate)
                put("symptoms", period.symptoms)
                put("mood", period.mood)
                put("notes", period.notes)
            }
            jsonArray.put(jsonObject)
        }

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(key, jsonArray.toString())
            .apply()
    }

    private fun getList(context: Context, key: String): List<PeriodEntity> {
        val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedData = preferences.getString(key, null) ?: return emptyList()

        return try {
            val jsonArray = JSONArray(savedData)
            val periods = mutableListOf<PeriodEntity>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                periods.add(
                    PeriodEntity(
                        userId = item.getInt("userId"),
                        startDate = item.getString("startDate"),
                        endDate = item.getString("endDate"),
                        symptoms = item.optString("symptoms", ""),
                        mood = item.optString("mood", ""),
                        notes = item.optString("notes", "")
                    )
                )
            }
            periods
        } catch (e: Exception) {
            emptyList()
        }
    }
}
