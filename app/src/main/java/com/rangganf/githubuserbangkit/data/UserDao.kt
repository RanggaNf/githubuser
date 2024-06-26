package com.rangganf.githubuserbangkit.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rangganf.githubuserbangkit.model.UserResponse

@Dao
interface UserDao {

    // Nglebokne pangguna menyang database. Yen pangguna karo ID sing padha wis ana, bakal diganti.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserResponse.Item)

    // Gwe Ngemot kabeh pangguna saka database minangka LiveData. LiveData ngijini nguwehi wektu nyata nalika data diganti.
    @Query("SELECT * FROM User")
    fun loadAll(): LiveData<MutableList<UserResponse.Item>>

    // Gwe nemokne pangguna kanthi ID lan mbalek e. Mnguwehi bates asil kanggo siji pangguna.
    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): UserResponse.Item

    // Gawe Hapus Pengguna Tekan Database.
    @Delete
    fun delete(user: UserResponse.Item)
}
