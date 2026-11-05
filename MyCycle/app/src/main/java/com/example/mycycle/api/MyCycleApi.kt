package com.example.mycycle.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

// ===============================
// USER REQUEST
// ===============================

data class UserRequest(
    val name: String = "",
    val email: String,
    val passwordHash: String
)

// USER RESPONSE
data class UserResponse(
    val message: String,
    val userId: Int,
    val name: String,
    val email: String
)

// ===============================
// PERIOD REQUEST
// ===============================

data class PeriodRequest(
    val userId: Int,
    val startDate: String,
    val endDate: String
)

// PERIOD RESPONSE
data class PeriodResponse(
    val message: String = "",
    val periodId: Int = 0,
    val userId: Int = 0,
    val startDate: String = "",
    val endDate: String = ""
)

// ===============================
// JOURNAL REQUEST
// ===============================

data class JournalRequest(
    val userId: Int,
    val symptoms: String,
    val mood: String,
    val entryDate: String
)

// JOURNAL RESPONSE
data class JournalResponse(
    val message: String,
    val journalId: Int
)

// ===============================
// AZURE BLOB STORAGE
// ===============================

data class BlobUploadResponse(
    val message: String,
    val fileName: String
)

// ===============================
// COSMOS DB REQUEST
// ===============================

data class CosmosCycleRecordRequest(
    val userId: String,
    val startDate: String,
    val endDate: String?,
    val symptoms: List<String>,
    val mood: String,
    val notes: String
)

// COSMOS DB RESPONSE
data class CosmosCycleRecordResponse(
    val id: String,
    val userId: String,
    val startDate: String,
    val endDate: String?,
    val symptoms: List<String>,
    val mood: String,
    val notes: String,
    val createdAt: String
)

// COSMOS SAVE RESPONSE
data class CosmosSaveResponse(
    val message: String,
    val recordId: String
)

// ===============================
// RETROFIT API INTERFACE
// ===============================

interface MyCycleApi {

    // USER REGISTRATION
    @POST("api/Users/register")
    suspend fun registerUser(
        @Body request: UserRequest
    ): Response<UserResponse>

    // USER LOGIN
    @POST("api/Users/login")
    suspend fun loginUser(
        @Body request: UserRequest
    ): Response<UserResponse>

    // DELETE USER
    @DELETE("api/Users/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: Int
    ): Response<Void>

    // SAVE PERIOD
    @POST("api/Periods")
    suspend fun savePeriod(
        @Body request: PeriodRequest
    ): Response<PeriodResponse>

    // GET PERIODS
    @GET("api/Periods/{userId}")
    suspend fun getPeriods(
        @Path("userId") userId: Int
    ): Response<List<PeriodResponse>>

    // SAVE JOURNAL
    @POST("api/Journal")
    suspend fun saveJournal(
        @Body request: JournalRequest
    ): Response<JournalResponse>

    // GET JOURNAL
    @GET("api/Journal/{userId}")
    suspend fun getJournal(
        @Path("userId") userId: Int
    ): Response<List<JournalResponse>>

    // AZURE BLOB IMAGE UPLOAD
    @Multipart
    @POST("api/BlobStorage/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<BlobUploadResponse>

    // ===============================
    // COSMOS DB - SAVE CYCLE RECORD
    // ===============================

    @POST("api/CosmosDb/save")
    suspend fun saveCycleRecord(
        @Body record: CosmosCycleRecordRequest
    ): Response<CosmosSaveResponse>

    // ===============================
    // COSMOS DB - GET CYCLE RECORDS
    // ===============================

    @GET("api/CosmosDb/records/{userId}")
    suspend fun getCycleRecords(
        @Path("userId") userId: String
    ): Response<List<CosmosCycleRecordResponse>>
}
