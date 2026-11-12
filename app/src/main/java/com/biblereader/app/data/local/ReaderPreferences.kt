package com.biblereader.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.readerDataStore by preferencesDataStore(name = "reader_preferences")

class ReaderPreferences(private val context: Context) {
    private val fontSizeKey = floatPreferencesKey("font_size")
    private val planDaysKey = stringPreferencesKey("completed_reading_plan_days")
    val fontSize = context.readerDataStore.data.map { it[fontSizeKey] ?: 23f }
    val completedPlanDays = context.readerDataStore.data.map { preferences ->
        preferences[planDaysKey].orEmpty().split(",").mapNotNull { it.toIntOrNull() }.toSet()
    }

    suspend fun setFontSize(value: Float) {
        context.readerDataStore.edit { it[fontSizeKey] = value.coerceIn(17f, 34f) }
    }

    suspend fun setPlanDayCompleted(day: Int, completed: Boolean) {
        context.readerDataStore.edit { preferences ->
            val days = preferences[planDaysKey].orEmpty().split(",").mapNotNull { it.toIntOrNull() }.toMutableSet()
            if (completed) days += day else days -= day
            preferences[planDaysKey] = days.sorted().joinToString(",")
        }
    }
}
