import pytube
import os
from os.path import join
import ffmpeg

class YTDownloader:
    def __init__(self, url):
        self.url = url
        self.youtube = pytube.YouTube(url)

    def get_title(self):
        youtube = self.youtube
        return youtube.title

    def get_thumbnail(self):
        youtube = self.youtube
        return youtube.thumbnail_url

    def get_video_streams(self):
        youtube = self.youtube
        return youtube.streams.filter(file_extension="mp4", type="video", adaptive=True)

    def get_audio_streams(self):
        youtube = self.youtube
        return youtube.streams.filter(only_audio=True, adaptive=True, mime_type="audio/mp4")

    def get_file_path(self):
        return os.environ['HOME']

    def video_download(self, itag):
        youtube = self.youtube
        dir = join(os.environ['HOME'], "video")
        youtube.streams.get_by_itag(itag).download(output_path=dir, filename="video.mp4")

    def audio_download(self, itag):
        youtube = self.youtube
        dir = join(os.environ['HOME'], "audio")
        youtube.streams.get_by_itag(itag).download(output_path=dir, filename="audio.m4a")

    def merge_audio_video(self):
        pass
        # input_video = ffmpeg.input(join(os.environ['HOME'], "video", "video.mp4"))
        # input_audio = ffmpeg.input(join(os.environ['HOME'], "audio", "audio.m4a"))
        # dir = join(os.environ['HOME'], "output", "output.mp4")


def video_title(video_url):
    yt = YTDownloader(video_url)
    return yt.get_title()

def video_thumbnail(video_url):
    yt = YTDownloader(video_url)
    return yt.get_thumbnail()

def video_streams(video_url):
    yt = YTDownloader(video_url)
    return yt.get_video_streams()

def audio_streams(video_url):
    yt = YTDownloader(video_url)
    return yt.get_audio_streams()

def file_path(video_url):
    yt = YTDownloader(video_url)
    return yt.get_file_path()

def video_download(video_url, i_tag):
    yt = YTDownloader(video_url)
    yt.video_download(itag=i_tag)

def audio_download(video_url, i_tag):
    yt = YTDownloader(video_url)
    yt.audio_download(itag=i_tag)

def audio_video_merge(video_url):
    yt = YTDownloader(video_url)
    yt.merge_audio_video()


