package com.ypm.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "youtube_id", nullable = false, length = Integer.MAX_VALUE, unique = true)
    private String youtubeId;

    @Column(name = "import_date")
    private LocalDate importDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @OneToOne(mappedBy = "video", cascade = CascadeType.ALL)
    private VideoData videoData;

    public Video(String youtubeId, LocalDate importDate) {
        this.youtubeId = youtubeId;
        this.importDate = importDate;
    }

    public Video(String youtubeId, LocalDate importDate, Playlist playlist) {
        this.youtubeId = youtubeId;
        this.importDate = importDate;
        this.playlist = playlist;
    }
}
