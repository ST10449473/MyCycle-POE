package com.example.mycycle.api

data class CycleRecordRequest(
    val userId: String,
    val startDate: String,
    val endDate: String?,
    val symptoms: List<String>,
    val mood: String,
    val notes: String
)