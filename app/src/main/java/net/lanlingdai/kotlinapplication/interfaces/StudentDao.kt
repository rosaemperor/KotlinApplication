package net.lanlingdai.kotlinapplication.interfaces

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import net.lanlingdai.kotlinapplication.entity.Student

@Dao
interface StudentDao{
    @Insert
fun insert(student: Student)
    @Query("DELETE FROM Student")
fun deleteAll()
    @Query("SELECT * from Student ")
fun getAllStudents() : List<Student>

@Insert
//    @Query("INSERT INTO Student(ID,name) VALUES (1,zhangsan)")
fun insertOrUpdate(student: Student)
}
