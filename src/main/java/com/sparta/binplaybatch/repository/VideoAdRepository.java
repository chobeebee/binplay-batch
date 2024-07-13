package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoAdRepository extends JpaRepository<VideoAd, Long> {
}
