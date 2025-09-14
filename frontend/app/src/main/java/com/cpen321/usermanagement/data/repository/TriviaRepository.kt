package com.cpen321.usermanagement.data.repository

import android.util.Log
import com.cpen321.usermanagement.data.remote.api.TriviaAPI
import com.cpen321.usermanagement.data.remote.api.TriviaQuestion
import com.cpen321.usermanagement.data.remote.api.TriviaRetrofit
import com.cpen321.usermanagement.trivia.HobbyToCategoryMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class TriviaRepository @Inject constructor(
    private val api: TriviaAPI
) {
    suspend fun getOne(hobbies: List<String>): Result<TriviaQuestion> = try {
        val categories = HobbyToCategoryMap.mapHobbies(hobbies)
        val chosen = categories[Random.nextInt(categories.size)]
        Log.d("TriviaRepository", "Chosen category: $chosen")
        val res = api.getTriviaQuestion(category = chosen)
        Log.d("TriviaRepository", "Response: $res")

        if (res.isSuccessful && !res.body().isNullOrEmpty()) {
            Result.success(res.body()!!.first())
        } else {
            Result.failure(Exception("No question found"))
        }
    } catch (t: Throwable) {
        Result.failure(t)
    }
}