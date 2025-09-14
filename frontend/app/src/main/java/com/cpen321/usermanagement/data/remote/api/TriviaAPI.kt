package com.cpen321.usermanagement.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaAPI {
    @GET("questions")
    suspend fun getTriviaQuestion(
        @Query("limit") amount: Int =1,
        @Query("categories") category: String,
        @Query("difficulties") difficulty: String = "easy,medium,hard",
        @Query("types") types: String = "text_choice"
    ): Response<List<TriviaQuestion>>
}

data class TriviaQuestion(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: QuestionText,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
)

data class QuestionText(val text: String)
