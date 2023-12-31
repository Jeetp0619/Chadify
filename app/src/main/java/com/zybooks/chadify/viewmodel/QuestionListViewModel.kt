package com.zybooks.chadify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zybooks.chadify.model.Question
import com.zybooks.chadify.repo.StudyRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuestionListViewModel(application: Application) : AndroidViewModel(application) {

    private val studyRepo = StudyRepository.getInstance(application)

    private val subjectIdLiveData = MutableLiveData<Long>()

    val questionListLiveData: LiveData<List<Question>> =
        Transformations.switchMap(subjectIdLiveData) { subjectId ->
            studyRepo.getQuestions(subjectId)
        }

    fun loadQuestions(subjectId: Long) {
        subjectIdLiveData.value = subjectId
    }

    fun addQuestion(question: Question) = viewModelScope.launch {
        studyRepo.addQuestion(question)
    }

    fun deleteQuestion(question: Question) = viewModelScope.launch {
        studyRepo.deleteQuestion(question)
    }
}