package com.ypm.persistence.repository;

import com.ypm.persistence.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findVideoByYoutubeId(String id);
}
