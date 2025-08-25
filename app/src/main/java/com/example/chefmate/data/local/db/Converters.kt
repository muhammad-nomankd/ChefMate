package com.example.chefmate.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun listToString(value: List<String>): String {
        return Gson().toJson(value)

    }

    @TypeConverter
    fun stringToList(value: String): List<String> {
        val typeList = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, typeList)
    }
}
