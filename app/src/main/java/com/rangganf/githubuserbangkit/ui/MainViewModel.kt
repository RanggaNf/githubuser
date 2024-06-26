package com.rangganf.githubuserbangkit.ui

import androidx.lifecycle.*
import com.rangganf.githubuserbangkit.data.local.SettingPreferences
import com.rangganf.githubuserbangkit.data.remote.ApiClient
import com.rangganf.githubuserbangkit.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {

    val resultUser = MutableLiveData<Result>()

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    private fun <T> performApiCall(call: suspend () -> T) {
        viewModelScope.launch {
            flow {
                emit(call())
            }.onStart {
                resultUser.value = Result.Loading(true)
            }.onCompletion {
                resultUser.value = Result.Loading(false)
            }.catch { throwable ->
                throwable.printStackTrace()
                resultUser.value = Result.Error(throwable)
            }.collect {
                resultUser.value = Result.Success(it)
            }
        }
    }

    fun getUser() {
        performApiCall {
            ApiClient.apiService.getUserGithub()
        }
    }

    fun getUser(username: String) {
        performApiCall {
            ApiClient.apiService.searchUserGithub(
                mapOf(
                    "q" to username,
                    "per_page" to 20
                )
            )
        }
    }

    class Factory(private val preferences: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(preferences) as T
    }
}
