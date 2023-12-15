package com.example.playlistmaker.data.dto

data class TrackDto(
    val trackName: String, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String?,
    val trackId: String, // Ссылка на изображение обложки
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String,
    val previewUrl: String?
)