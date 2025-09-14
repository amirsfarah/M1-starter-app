package com.cpen321.usermanagement.trivia

object HobbyToCategoryMap {
    private val map = mapOf(
        "Reading" to "arts_and_literature",
        "Writing" to "arts_and_literature",
        "Photography" to "arts_and_literature",
        "Cooking" to "food_and_drink",
        "Gardening" to "science",
        "Painting" to "arts_and_literature",
        "Drawing" to "arts_and_literature",
        "Pottery" to "arts_and_literature",
        "Running" to "sport_and_leisure",
        "Yoga" to "sport_and_leisure",
        "Chess" to "sport_and_leisure",
        "Video Games" to "general_knowledge",
        "Travel" to "geography",
        "Coding" to "science",
        "Blogging" to "society_and_culture",
        "Surfing" to "sport_and_leisure",
        "Skiing" to "sport_and_leisure",
        "Singing" to "music"
    )

    fun mapHobbies(hobbies: List<String>): List<String> =
        hobbies.mapNotNull { map[it] }.distinct().ifEmpty { listOf("general_knowledge") }
}