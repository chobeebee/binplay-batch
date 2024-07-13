package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Videos, Long> {
    List<Videos> findAll();
}
