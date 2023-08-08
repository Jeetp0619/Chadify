package com.zybooks.chadify.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zybooks.chadify.model.Question
import com.zybooks.chadify.model.Subject

@Database(entities = [Question::class, Subject::class], version = 2)
abstract class StudyDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun subjectDao(): SubjectDao
}