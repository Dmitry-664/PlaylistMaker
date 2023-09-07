package com.example.playlistmaker

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    var trackTime: Long, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)