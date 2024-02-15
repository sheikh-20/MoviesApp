package com.application.moviesapp.data.python

import com.chaquo.python.Python
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

class DownloaderRepositoryImpl @Inject constructor(private val python: Python): DownloaderRepository {
    override suspend fun getTitle(videoUrl: String): String {
        return python.getModule("YoutubeDownloader").callAttr("video_title", videoUrl).toString()
    }

    override suspend fun getThumbnail(videoUrl: String): String {
        return python.getModule("YoutubeDownloader").callAttr("video_thumbnail", videoUrl).toString()
    }

    override suspend fun getVideoStreams(videoUrl: String): String {
        return python.getModule("YoutubeDownloader").callAttr("video_streams", videoUrl).toString()
    }

    override suspend fun getAudioStreams(videoUrl: String): String {
        return python.getModule("YoutubeDownloader").callAttr("audio_streams", videoUrl).toString()
    }

    override suspend fun getFilePath(videoUrl: String): String {
        return python.getModule("YoutubeDownloader").callAttr("file_path", videoUrl).toString()
    }

    override suspend fun videoDownload(videoUrl: String, iTag: Int) {
        python.getModule("YoutubeDownloader").callAttr("video_download", videoUrl, iTag)
    }

    override suspend fun audioDownload(videoUrl: String, iTag: Int) {
        python.getModule("YoutubeDownloader").callAttr("audio_download", videoUrl, iTag)
    }

    override suspend fun mergeAudioVideo(videoUrl: String) {
        python.getModule("YoutubeDownloader").callAttr("audio_video_merge", videoUrl)
    }
}