package com.rangganf.githubuserbangkit.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rangganf.githubuserbangkit.data.local.DatabaseModule

// Comment: This is a ViewModel class for the Favorite feature in the application.
class FavoriteViewModel(private val dbModule: DatabaseModule) : ViewModel() {

    // Comment: This function is used to retrieve a list of favorite users from the database.
    fun getUserFavorite() = dbModule.userDao.loadAll()

    // Comment: This is a Factory class used to create an instance of FavoriteViewModel.
    class Factory(private val db: DatabaseModule) : ViewModelProvider.NewInstanceFactory() {
        // Comment: This function is used to create an instance of FavoriteViewModel with DatabaseModule as a parameter.
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoriteViewModel(db) as T
    }
}
