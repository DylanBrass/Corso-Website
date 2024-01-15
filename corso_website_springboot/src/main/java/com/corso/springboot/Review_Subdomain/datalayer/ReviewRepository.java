package com.corso.springboot.Review_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {


    List<Review> getReviewByPinned(boolean pinned);

//    @Query("SELECT r FROM Review r WHERE r.id = :reviewId")
//    List<Review> getReviewById(@Param("reviewId") String reviewId);

    Review findReviewByReviewId_ReviewId(String reviewId);



    @Query("SELECT r FROM Review r WHERE r.customerFullName LIKE ?1%")
    List<Review> findAllByCustomerFullNameStartingWith(String customerFullName);

    List<Review> findAllByUserId(String userId);


    @Query(value = "SELECT CASE WHEN EXISTS ( SELECT 1 FROM reviews WHERE user_id = :userId AND review_date > CURRENT_DATE() - INTERVAL '7' DAY) THEN 'true' ELSE 'false' END AS has_reviewed;", nativeQuery = true)
    Boolean reviewInLastWeekForUser(@Param("userId") String userId);

        int countAllByPinned(boolean pinned);

        int countAllBy();

        @Query(value = "SELECT * FROM reviews ORDER BY id DESC LIMIT :#{#pageSize} OFFSET :#{#offset}", nativeQuery = true)
        List<Review> findAllReviews(@Param("pageSize") int pageSize, @Param("offset") int offset);



}
