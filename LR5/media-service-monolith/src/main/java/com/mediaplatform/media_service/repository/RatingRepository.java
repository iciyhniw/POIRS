package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select avg(r.score) from Rating r where r.contentId = :contentId")
    Double findAverageScoreByContentId(@Param("contentId") Long contentId);
}