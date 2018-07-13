package net.lanlingdai.kotlinapplication.dao

import net.lanlingdai.kotlinapplication.application.KotlinApplication
import net.lanlingdai.kotlinapplication.entity.Student
import net.lanlingdai.kotlinapplication.interfaces.StudentDao

class StudentRespository constructor(application: KotlinApplication){
     var studentRoomDatabase : StudentRoomDatabase?
    var studentDao : StudentDao
    init {
        studentRoomDatabase = StudentRoomDatabase.getDatabase(application)
        studentDao = studentRoomDatabase!!.getStudentDao()
    }

    fun insert(student: Student){
        studentDao.insert(student)
    }

    fun getStudents(): List<Student> {
        return studentDao.getAllStudents()
    }
}
