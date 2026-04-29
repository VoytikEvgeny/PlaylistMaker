package com.example.playlistmaker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Track

        return trackId == other.trackId
    }

    override fun hashCode(): Int {
        return trackId
    }

    fun getFormattedTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }

    fun getHighResArtworkUrl(): String {
        return artworkUrl100.replace("100x100bb", "512x512bb")
    }

    fun getReleaseYear(): String? {
        return releaseDate.take(4)
    }

}