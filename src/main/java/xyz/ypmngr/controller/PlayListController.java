package xyz.ypmngr.controller;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistSnippet;
import xyz.ypmngr.dto.PlaylistDto;
import xyz.ypmngr.dto.request.MergePlayListsRequest;
import xyz.ypmngr.service.AuthService;
import xyz.ypmngr.service.youtube.PlayListService;
import xyz.ypmngr.service.youtube.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlayListController {

    private final PlayListService playListService;
    private final VideoService videosService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getPlayLists() throws IOException {
        return ResponseEntity.ok(playListService.getPlayLists(authService.getToken()));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<List<PlaylistItem>> getPlayListVideos(@PathVariable String playlistId) throws IOException {
        var playlistVideos = videosService.getPlayListVideos(authService.getToken(), playlistId);
        return ResponseEntity.ok(playlistVideos);
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody PlaylistDto playlistDto) throws IOException {
        var createdPlayList = playListService.createPlayList(authService.getToken(), playlistDto);
        return ResponseEntity.ok(createdPlayList);
    }

    @PutMapping
    public ResponseEntity<Playlist> mergePlayLists(@RequestBody MergePlayListsRequest request) throws IOException {
        var mergedPlaylist = playListService.mergePlayLists(authService.getToken(), request.playlistDto(),
                                                            request.playListsIds(), request.deleteAfterMerge());
        return ResponseEntity.ok(mergedPlaylist);
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<Playlist> updatePlayListTitle(
            @PathVariable String playlistId,
            @RequestBody PlaylistSnippet dataWithUpdatedTitle
    ) throws IOException {
        var newTitle = dataWithUpdatedTitle.getTitle();
        var updatedPlaylist = playListService.updatePlayListTitle(authService.getToken(), playlistId, newTitle);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @PutMapping("/{playlistId}/{targetPlaylistId}")
    public ResponseEntity<List<PlaylistItem>> moveVideos(
            @PathVariable String playlistId,
            @PathVariable String targetPlaylistId,
            @RequestBody List<String> videosIds
    ) throws IOException {
        var movedVideos = videosService.moveVideos(authService.getToken(), playlistId, targetPlaylistId, videosIds);
        return ResponseEntity.ok(movedVideos);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlayList(@PathVariable String playlistId) throws IOException {
        playListService.deletePlayList(authService.getToken(), playlistId);
        return ResponseEntity.noContent().build();
    }
}
