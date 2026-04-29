package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackMapperImpl : TrackMapper {

    override fun map(trackDto: TrackDto): Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis,
            artworkUrl100 = trackDto.artworkUrl100,
            previewUrl = trackDto.previewUrl,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country
        )
    }

    override fun reversedMap(track: Track): TrackDto {
        val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
        return TrackDto(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country
        )
    }
}