package com.rangganf.githubuserbangkit.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rangganf.githubuserbangkit.data.local.DatabaseModule
import com.rangganf.githubuserbangkit.model.UserResponse
import com.rangganf.githubuserbangkit.data.remote.ApiClient
import com.rangganf.githubuserbangkit.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val db: DatabaseModule) : ViewModel() {

    // LiveData for user detail results
    private val _resultDetailUser = MutableLiveData<Result>()
    val resultDetailUser: LiveData<Result>
        get() = _resultDetailUser

    private val _resultFollowersUser = MutableLiveData<Result>()
    val resultFollowersUser: LiveData<Result>
        get() = _resultFollowersUser

    private val _resultFollowingUser = MutableLiveData<Result>()
    val resultFollowingUser: LiveData<Result>
        get() = _resultFollowingUser

    private val _resultSuksesFavorite = MutableLiveData<Boolean>()
    val resultSuksesFavorite: LiveData<Boolean>
        get() = _resultSuksesFavorite

    private val _resultDeleteFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite: LiveData<Boolean>
        get() = _resultDeleteFavorite

    private var isFavorite = false

    // Function to set user as favorite or not
    fun setFavorite(item: UserResponse.Item?) {
        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    db.userDao.delete(item)
                    _resultDeleteFavorite.value = true
                } else {
                    db.userDao.insert(item)
                    _resultSuksesFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    // Function to find favorite user
    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null) {
                listenFavorite()
                isFavorite = true
            }
        }
    }

    // Function to get user detail
    fun getDetailUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .apiService
                    .getDetailUserGithub(username)

                emit(response)
            }.onStart {
                _resultDetailUser.value = Result.Loading(true)
            }.onCompletion {
                _resultDetailUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _resultDetailUser.value = Result.Error(it)
            }.collect {
                _resultDetailUser.value = Result.Success(it)
            }
        }
    }

    // Function to get user followers
    fun getFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .apiService
                    .getFollowersUserGithub(username)

                emit(response)
            }.onStart {
                _resultFollowersUser.value = Result.Loading(true)
            }.onCompletion {
                _resultFollowersUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _resultFollowersUser.value = Result.Error(it)
            }.collect {
                _resultFollowersUser.value = Result.Success(it)
            }
        }
    }

    // Function to get user following
    fun getFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .apiService
                    .getFollowingUserGithub(username)

                emit(response)
            }.onStart {
                _resultFollowingUser.value = Result.Loading(true)
            }.onCompletion {
                _resultFollowingUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _resultFollowingUser.value = Result.Error(it)
            }.collect {
                _resultFollowingUser.value = Result.Success(it)
            }
        }
    }

    // Factory for ViewModel
    class Factory(private val db: DatabaseModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}


