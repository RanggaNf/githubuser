package com.rangganf.githubuserbangkit.utils

// Sealed class `Result` memiliki tiga tipe turunan: Success, Error, dan Loading.
sealed class Result {

    // Data class `Success` saya gunakan untuk mengindikasikan operasi berhasil dengan data yang diberikan.
    data class Success<out T>(val data: T) : Result()

    // Data class `Error` ini saya gunakan untuk mengindikasikan operasi gagal dengan pengecualian (exception) yang terjadi.
    data class Error(val exception: Throwable) : Result()

    // Data class `Loading` saya gunakan untuk mengindikasikan bahwa operasi sedang dalam proses atau loading.
    data class Loading(val isLoading: Boolean) : Result()
}
