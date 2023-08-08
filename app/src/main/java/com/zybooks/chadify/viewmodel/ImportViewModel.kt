package com.zybooks.chadify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zybooks.chadify.model.Subject
import com.zybooks.chadify.repo.StudyRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ImportViewModel(application: Application) : AndroidViewModel(application) {

    private val studyRepo = StudyRepository.getInstance(application)

    var importedSubject = studyRepo.importedSubject
    var fetchedSubjectList = studyRepo.fetchedSubjectList

    fun addSubject(subject: Subject) = viewModelScope.launch {
        studyRepo.addSubject(subject)
        studyRepo.fetchQuestions(subject)
    }

    fun fetchSubjects() = studyRepo.fetchSubjects()
}