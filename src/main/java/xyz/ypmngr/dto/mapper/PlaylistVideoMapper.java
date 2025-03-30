package xyz.ypmngr.dto.mapper;

import xyz.ypmngr.model.Video;

import java.util.List;

public class PlaylistVideoMapper {
    public static List<Video> mapAll(List<com.google.api.services.youtube.model.PlaylistItem> ytPlaylistsVideos) {
        return ytPlaylistsVideos.stream().map(PlaylistVideoMapper::map).toList();
    }

    public static Video map(com.google.api.services.youtube.model.PlaylistItem ytPlaylistVideo) {
        return new Video()
                .title(ytPlaylistVideo.getSnippet().getTitle())
                .globalId(ytPlaylistVideo.getContentDetails().getVideoId())
                .playlistItemId(ytPlaylistVideo.getId())
                .playlistId(ytPlaylistVideo.getSnippet().getPlaylistId())
                .ownerChannelId(ytPlaylistVideo.getSnippet().getVideoOwnerChannelId())
                .ownerChannelTitle(ytPlaylistVideo.getSnippet().getVideoOwnerChannelTitle());
    }
}
