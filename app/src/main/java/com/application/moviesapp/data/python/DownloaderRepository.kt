package com.application.moviesapp.data.python

import javax.inject.Inject

interface DownloaderRepository {
    suspend fun getTitle(videoUrl: String): String
    suspend fun getThumbnail(videoUrl: String): String
    suspend fun getVideoStreams(videoUrl: String): String
    suspend fun getAudioStreams(videoUrl: String): String
    suspend fun getFilePath(videoUrl: String): String
    suspend fun videoDownload(videoUrl: String, iTag: Int)
    suspend fun audioDownload(videoUrl: String, iTag: Int)
    suspend fun mergeAudioVideo(videoUrl: String)
}

class DownloaderRepositoryImpl @Inject constructor(): DownloaderRepository {
    override suspend fun getTitle(videoUrl: String): String {
        return ""
    }

    override suspend fun getThumbnail(videoUrl: String): String {
        return ""
    }

    override suspend fun getVideoStreams(videoUrl: String): String {
        return ""
    }

    override suspend fun getAudioStreams(videoUrl: String): String {
        return ""
    }

    override suspend fun getFilePath(videoUrl: String): String {
        return ""
    }

    override suspend fun videoDownload(videoUrl: String, iTag: Int) {

    }

    override suspend fun audioDownload(videoUrl: String, iTag: Int) {

    }

    override suspend fun mergeAudioVideo(videoUrl: String) {

    }
}