MyCycle
An Android menstrual cycle tracking app built with Kotlin and Jetpack Compose, backed by an ASP.NET Core Web API with Azure Blob Storage and Azure Cosmos DB.

Features
Record period start and end dates
Log symptoms, mood and notes per cycle
Upload images to Azure Blob Storage
Save and retrieve cycle records from Azure Cosmos DB (NoSQL)
User accounts with encrypted password storage
Tech Stack
Layer	Technology
Android UI	Kotlin, Jetpack Compose
Networking	Retrofit, OkHttp
Backend	ASP.NET Core Web API
File storage	Azure Blob Storage
Data storage	Azure Cosmos DB (NoSQL)
API testing	Swagger
Project Structure


app/
└── src/main/java/com/example/mycycle/
    └── api/
        └── MyCycleApi.kt       # Retrofit interface + request/response models
API Reference
All endpoints are defined in MyCycleApi.kt.

Periods
kotlin


@POST("api/Periods")
suspend fun createPeriod(
    @Body request: PeriodRequest
): Response<PeriodResponse>
Cosmos DB — save a cycle record
kotlin


@POST("api/CosmosDb/save")
suspend fun saveCycleRecord(
    @Body record: CosmosCycleRecordRequest
): Response<CosmosSaveResponse>
Cosmos DB — retrieve cycle records
kotlin


@GET("api/CosmosDb/records/{userId}")
suspend fun getCycleRecords(
    @Path("userId") userId: String
): Response<List<CosmosCycleRecordResponse>>
Data Models
kotlin


data class PeriodRequest(
    val userId: Int,
    val startDate: String,
    val endDate: String
)

data class PeriodResponse(
    val message: String,
    val periodId: Int
)

data class CosmosCycleRecordRequest(
    val userId: String,
    val startDate: String,
    val endDate: String?,
    val symptoms: List<String>,
    val mood: String,
    val notes: String
)

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

data class CosmosSaveResponse(
    val message: String,
    val recordId: String
)
Only CosmosCycleRecordRequest and CosmosCycleRecordResponse differ in shape from one another: the response carries the server-assigned id and createdAt.

Cosmos DB Setup
Create an Azure Cosmos DB account.
Create a database with a container named CycleRecords.
Set the partition key to userId.
Add the connection string to the backend configuration.
Cycle records are scoped per user by userId, which is both the partition key and the lookup path parameter.

Build and Run
Open the project in Android Studio.
Set the backend base URL in the Retrofit builder.
Select Build → Make Project.
Expect BUILD SUCCESSFUL before deploying.
Run on a physical phone with USB debugging enabled.
Test backend save and retrieve operations through Swagger before testing from the device.

Testing Checklist
 Build completes with BUILD SUCCESSFUL
 Period saved via api/Periods
 Cycle record saved to Cosmos DB and confirmed in Swagger
 Cycle records retrieved for a user
 Image upload to Blob Storage returns a file name
 App runs and demonstrates the above on a physical phone
Privacy Notes
MyCycle stores personal cycle-tracking data. Keep Azure connection strings and user records out of source control. Cycle predictions are estimates only and are not medical advice or a form of contraception.

AI Use
AI (ChatGPT) was used as a supporting tool during development — for explaining Kotlin, Jetpack Compose and Retrofit concepts, understanding Azure Blob Storage and Cosmos DB, structuring the Retrofit interface, and debugging build and API errors. All suggestions were reviewed, adapted and tested by the developer, who remains responsible for the implementation, configuration and verification of the app..
