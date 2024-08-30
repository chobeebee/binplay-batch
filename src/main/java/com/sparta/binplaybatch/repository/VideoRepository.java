package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Videos, Long> {
    //모든 비디오 찾기
    List<Videos> findAll();
}
