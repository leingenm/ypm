package xyz.ypmngr.controller;

import xyz.ypmngr.dto.VideoDto;
import xyz.ypmngr.service.AuthService;
import xyz.ypmngr.service.youtube.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videosService;
    private final AuthService authService;

    @PostMapping("/load")
    public ResponseEntity<List<VideoDto>> getVideoData(@RequestBody List<String> videoIds) throws IOException {
        var videoData = videosService.getVideoData(videoIds);

        return ResponseEntity.ok(videoData);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideos(
            @PathVariable String videoId
    ) throws IOException {
        videosService.deleteVideo(authService.getToken(), videoId);
        return ResponseEntity.noContent().build();
    }
}
