package com.cpen321.usermanagement.ui.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpen321.usermanagement.data.remote.api.TriviaQuestion
import com.cpen321.usermanagement.data.repository.TriviaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class TriviaCardUiState(
    val isLoading: Boolean = false,
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = -1,
    val category: String = "",
    val difficulty: String = "",
    val error: String? = null,
    val revealed: Boolean = false
)

@HiltViewModel
class TriviaCardViewModel @Inject constructor(
//    private val profileViewModel: ProfileViewModel,
    private val repo: TriviaRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(TriviaCardUiState())
    val ui = _ui.asStateFlow()

    fun loadNewQuestion(hobbies: List<String>) {
        //val hobbies = selectedHobbies.orEmpty()

//         val hobbies: StateFlow<Set<String>> = profileViewModel.uiState
//            .map { it.selectedHobbies }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = emptySet()
//            )

        _ui.value = _ui.value.copy(isLoading = true, error = null, revealed = false)
        viewModelScope.launch {
            val result = repo.getOne(hobbies as List<String>)
            if (result.isSuccess) {
                val q = result.getOrNull()!!
                val (opts, correctIdx) = shuffleChoices(q)
                _ui.value = _ui.value.copy(
                    isLoading = false,
                    question = q.question.text,
                    options = opts,
                    correctIndex = correctIdx,
                    category = q.category,
                    difficulty = q.difficulty,
                    revealed = false
                )
            } else {
                _ui.value = _ui.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to load question"
                )
            }
        }
    }

    fun reveal() { _ui.value = _ui.value.copy(revealed = true) }

    private fun shuffleChoices(q: TriviaQuestion): Pair<List<String>, Int> {
        if (q.incorrectAnswers.isEmpty() || q.correctAnswer.isEmpty() || q.correctAnswer == null) {
            return listOf(q.correctAnswer) to 0
        }
        val all = q.incorrectAnswers.toMutableList().apply { add(q.correctAnswer) }
        val shuffled = all.shuffled(Random(System.nanoTime()))
        val correctIdx = shuffled.indexOf(q.correctAnswer)
        return shuffled to correctIdx
    }
}
