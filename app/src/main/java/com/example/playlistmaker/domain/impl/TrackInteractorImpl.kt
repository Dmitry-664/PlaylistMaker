package com.example.playlistmaker.domain.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {
    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newCachedThreadPool() //в фоновом потоке
    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            val tracks = repository.searchTracks(expression)
            handler.post {
                consumer.consume(tracks)
            }
        }
    }
}