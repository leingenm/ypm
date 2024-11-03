package com.ypm.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "video_data", schema = "ypm")
public class VideoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "channel_name", length = Integer.MAX_VALUE)
    private String channelName;

    @Column(name = "tags", length = Integer.MAX_VALUE)
    private String tags;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    public VideoData(String title, String description, String channelName, String tags) {
        this.title = title;
        this.description = description;
        this.channelName = channelName;
        this.tags = tags;
    }
}
