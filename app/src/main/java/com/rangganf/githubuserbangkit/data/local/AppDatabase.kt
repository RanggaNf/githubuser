package com.rangganf.githubuserbangkit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rangganf.githubuserbangkit.data.UserDao
import com.rangganf.githubuserbangkit.model.UserResponse

// Kelas AppDatabase mewakili database lokal (Room Database) yang digunakan dalam aplikasi.
@Database(entities = [UserResponse.Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Fungsi abstrak userDao() digunakan untuk mengakses objek Objek Akses Data (Dao) yang terkait dengan entitas pengguna.
    abstract fun userDao(): UserDao
}

