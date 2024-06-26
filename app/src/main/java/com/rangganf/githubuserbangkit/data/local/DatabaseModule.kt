package com.rangganf.githubuserbangkit.data.local

import android.content.Context
import androidx.room.Room

// Kelas DatabaseModule digunakake kanggo initialize lan nyedhiyani akses menyang database Kamar( Room database).
class DatabaseModule(private val context: Context) {
    // Initialize database Kamar nggunakake pola builder pattern.
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "usergithub.db")
        .allowMainThreadQueries() // Ngijini akses database saka utas utama (biasane digunakake kanggo testing).
        .build()

    // Nyedhiyani akses menyang UserDao, yaiku antarmuka akses data kanggo entitas pangguna.
    val userDao = db.userDao()
}
