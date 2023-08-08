package com.zybooks.chadify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.zybooks.chadify.model.Subject
import com.zybooks.chadify.repo.StudyRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SubjectListViewModel(application: Application) : AndroidViewModel(application) {

    private val studyRepo = StudyRepository.getInstance(application.applicationContext)

    val subjectListLiveData: LiveData<List<Subject>> = studyRepo.getSubjects()

    fun addSubject(subject: Subject) = viewModelScope.launch {
        studyRepo.addSubject(subject)
    }

    fun deleteSubject(subject: Subject) = viewModelScope.launch {
        studyRepo.deleteSubject(subject)
    }
}