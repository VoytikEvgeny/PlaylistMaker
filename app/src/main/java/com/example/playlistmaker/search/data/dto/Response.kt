package com.example.playlistmaker.search.data.dto

enum class ResponseStatus {
    SUCCESS,
    BAD_REQUEST,
    NO_INTERNET,
    SERVER_ERROR,
    UNKNOWN_ERROR
}

open class Response {
    var status: ResponseStatus = ResponseStatus.UNKNOWN_ERROR
}