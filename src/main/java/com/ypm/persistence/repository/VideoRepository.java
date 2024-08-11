package com.ypm.persistence.repository;

import com.ypm.persistence.entity.VideoImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<VideoImport, Long> {

}
