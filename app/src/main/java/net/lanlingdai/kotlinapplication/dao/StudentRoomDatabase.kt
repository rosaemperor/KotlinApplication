package net.lanlingdai.kotlinapplication.dao

import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.room.*
import android.content.Context
import net.lanlingdai.kotlinapplication.entity.Student
import net.lanlingdai.kotlinapplication.interfaces.StudentDao
import android.arch.persistence.room.Room



@Database(entities = arrayOf(Student::class), version = 1)
abstract class StudentRoomDatabase : RoomDatabase() {
    companion object {
       private  var instance : StudentRoomDatabase? = null

        fun getDatabase(context: Context): StudentRoomDatabase? {
            synchronized(StudentRoomDatabase::class.java){
                instance = Room.databaseBuilder(context.applicationContext,
                        StudentRoomDatabase::class.java  ,"student_database").build()

            }

            return instance




        }
    }
    abstract fun getStudentDao() : StudentDao

}