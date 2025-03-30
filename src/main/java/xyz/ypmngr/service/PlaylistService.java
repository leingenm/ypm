package xyz.ypmngr.service;

import xyz.ypmngr.model.Playlist;

import java.util.List;

public interface PlaylistService {

    List<Playlist> getAllUserPlaylists();

    Playlist getPlaylistById(String playListId);

    Playlist createPlaylist(Playlist playlist);

    void deletePlaylist(String playListId);

    Playlist updatePlaylistTitle(String playListId, String newTitle);

    Playlist mergePlaylists(Playlist playlist, List<String> playListsIds, boolean deleteAfterMerge);

    Playlist moveVideosBetweenPlaylists(String from, String to, List<String> videosIds, boolean deleteAfterMove);

    void removeVideoFromPlaylist(String videoId);

    void insertVideoIntoPlaylist(String videoGlobalId, String playlistId);
}
