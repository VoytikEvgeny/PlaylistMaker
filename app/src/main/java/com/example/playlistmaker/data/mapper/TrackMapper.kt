package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.model.Track


interface TrackMapper {
    fun map(trackDto: TrackDto): Track
    fun reversedMap(track: Track): TrackDto
}