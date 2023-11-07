package com.application.moviesapp.ui.utility

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaMuxer
import timber.log.Timber
import java.io.IOException
import java.nio.ByteBuffer

class Mp4ParserAudioMuxer(private val context: Context) {

    companion object {
        private const val TAG = "Mp4ParserAudioMuxer"
    }

    fun startMerging() {
        try {
            val videoExtractor = MediaExtractor()
            videoExtractor.setDataSource("${context.filesDir.path}/video/video.mp4" )
            val audioExtractor = MediaExtractor()
            audioExtractor.setDataSource("${context.filesDir.path}/audio/audio.m4a")
            val muxer = MediaMuxer("${context.filesDir.path}/output/output.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            videoExtractor.selectTrack(0)
            val videoFormat = videoExtractor.getTrackFormat(0)
            val videoTrack: Int = muxer.addTrack(videoFormat)
            audioExtractor.selectTrack(0)
            val audioFormat = audioExtractor.getTrackFormat(0)
            val audioTrack: Int = muxer.addTrack(audioFormat)
//            Log.d(TAG, "Video Format $videoFormat")
//            Log.d(TAG, "Audio Format $audioFormat")
            var sawEOS = false
            var frameCount = 0
            val offset = 100
            val sampleSize = 768 * 1024
            val videoBuf = ByteBuffer.allocate(sampleSize)
            val audioBuf = ByteBuffer.allocate(sampleSize)
            val videoBufferInfo = MediaCodec.BufferInfo()
            val audioBufferInfo = MediaCodec.BufferInfo()
            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            muxer.start()
            while (!sawEOS) {
                videoBufferInfo.offset = offset
                videoBufferInfo.size = videoExtractor.readSampleData(videoBuf, offset)
                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
//                    Log.d("GGv", "saw input EOS.")
                    sawEOS = true
                    videoBufferInfo.size = 0
                } else {
                    videoBufferInfo.presentationTimeUs = videoExtractor.sampleTime
                    videoBufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME
                    muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo)
                    videoExtractor.advance()
                    frameCount++
                    //Log.d(TAG, "Frame (" + frameCount + ") Video PresentationTimeUs:" + videoBufferInfo.presentationTimeUs +" Flags:" + videoBufferInfo.flags +" Size(KB) " + videoBufferInfo.size / 1024);
                    //Log.d(TAG, "Frame (" + frameCount + ") Audio PresentationTimeUs:" + audioBufferInfo.presentationTimeUs +" Flags:" + audioBufferInfo.flags +" Size(KB) " + audioBufferInfo.size / 1024);
                }
            }

            //Toast.makeText(getApplicationContext() , "frame:" + frameCount , Toast.LENGTH_SHORT).show();
            var sawEOS2 = false
            var frameCount2 = 0
            while (!sawEOS2) {
                frameCount2++
                audioBufferInfo.offset = offset
                audioBufferInfo.size = audioExtractor.readSampleData(audioBuf, offset)
                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
//                    Log.d("GGa", "saw input EOS.")
                    sawEOS2 = true
                    audioBufferInfo.size = 0
                } else {
                    audioBufferInfo.presentationTimeUs = audioExtractor.sampleTime
                    audioBufferInfo.flags = audioExtractor.sampleFlags
                    muxer.writeSampleData(audioTrack, audioBuf, audioBufferInfo)
                    audioExtractor.advance()
                }
            }
            muxer.stop()
            muxer.release()
        } catch (e: IOException) {
            Timber.tag(TAG).e(e.message.toString())
        }
    }
}