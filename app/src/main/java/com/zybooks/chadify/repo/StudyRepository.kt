package com.zybooks.chadify.repo

import android.content.Context
import androidx.room.Room
import com.zybooks.chadify.model.Question
import com.zybooks.chadify.model.Subject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudyRepository private constructor(context: Context) {
    var importedSubject = MutableLiveData<String>()
    var fetchedSubjectList = MutableLiveData<List<Subject>>()

    private val studyFetcher: StudyFetcher = StudyFetcher(context.applicationContext)

    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            CoroutineScope(Dispatchers.IO).launch {
                instance?.addStarterData()
            }
        }
    }

    fun getSubject(subjectId: Long): LiveData<Subject?> = subjectDao.getSubject(subjectId)

    fun getSubjects(): LiveData<List<Subject>> = subjectDao.getSubjects()

    fun getQuestion(questionId: Long): LiveData<Question?> = questionDao.getQuestion(questionId)

    fun getQuestions(subjectId: Long): LiveData<List<Question>> = questionDao.getQuestions(subjectId)

    companion object {
        private var instance: StudyRepository? = null

        fun getInstance(context: Context): StudyRepository {
            if (instance == null) {
                instance = StudyRepository(context)
            }
            return instance!!
        }
    }

    private val database : StudyDatabase = Room.databaseBuilder(
        context.applicationContext,
        StudyDatabase::class.java,
        "study.db"
    )
        .addCallback(databaseCallback)
        //.allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val subjectDao = database.subjectDao()
    private val questionDao = database.questionDao()



    suspend fun addSubject(subject: Subject) {
        subject.id = subjectDao.addSubject(subject)
    }

    suspend fun deleteSubject(subject: Subject) = subjectDao.deleteSubject(subject)


    suspend fun addQuestion(question: Question) {
        question.id = questionDao.addQuestion(question)
    }

    suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(question)

    suspend fun deleteQuestion(question: Question) = questionDao.deleteQuestion(question)

    private suspend fun addStarterData() {
        var subjectId = subjectDao.addSubject(Subject(text = "Math"))
        questionDao.addQuestion(
            Question(
                text = "What is 2 + 3?",
                answer = "2 + 3 = 5",
                subjectId = subjectId
            )
        )
        questionDao.addQuestion(
            Question(
                text = "What is pi?",
                answer = "The ratio of a circle's circumference to its diameter.",
                subjectId = subjectId
            )
        )

        subjectId = subjectDao.addSubject(Subject(text = "History"))
        questionDao.addQuestion(
            Question(
                text = "On what date was the U.S. Declaration of Independence adopted?",
                answer = "July 4, 1776",
                subjectId = subjectId
            )
        )

        subjectDao.addSubject(Subject(text = "Computing"))
    }


    fun fetchSubjects() = studyFetcher.fetchSubjects(fetchListener)

    fun fetchQuestions(subject: Subject) = studyFetcher.fetchQuestions(subject, fetchListener)

    private val fetchListener = object : StudyFetcher.OnStudyDataReceivedListener {
        override fun onSubjectsReceived(subjectList: List<Subject>) {
            fetchedSubjectList.value = subjectList
        }

        override fun onQuestionsReceived(subject: Subject, questionList: List<Question>) {
            for (question in questionList) {
                question.subjectId = subject.id
                CoroutineScope(Dispatchers.IO).launch {
                    addQuestion(question)
                }
            }

            importedSubject.value = subject.text
        }

        override fun onErrorResponse(error: VolleyError) {
            error.printStackTrace()
        }
    }
}