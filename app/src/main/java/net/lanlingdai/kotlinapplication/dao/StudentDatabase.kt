package net.lanlingdai.kotlinapplication.dao

import android.arch.persistence.room.RoomDatabase
import net.lanlingdai.kotlinapplication.entity.Student
import net.lanlingdai.kotlinapplication.interfaces.StudentDao

abstract class StudentDatabase : RoomDatabase(){
    abstract fun studentDao() : StudentDao
}