package com.example.playlistmaker.util

sealed class SearchResults<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): SearchResults<T>(data)
    class Error<T>(message: String, data: T? = null): SearchResults<T>(data, message)
}
