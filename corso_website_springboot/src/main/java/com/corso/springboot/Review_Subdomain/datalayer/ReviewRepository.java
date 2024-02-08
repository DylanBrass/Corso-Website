package com.corso.springboot.Review_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {


    List<Review> getReviewByPinned(boolean pinned);

    Review findReviewByReviewId_ReviewId(String reviewId);

    int countAllByPinned(boolean pinned);

    @Query(value = "SELECT * FROM reviews WHERE customer_full_name LIKE concat(:customerFullName,'%') ORDER BY pinned LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Review> findAllByCustomerFullNameStartingWithAndPagination(@Param("customerFullName") String customerFullName, @Param("pageSize") int pageSize, @Param("offset") int offset);

    List<Review> findAllByUserId(String userId);

    @Query(value = "SELECT * FROM reviews ORDER BY pinned DESC LIMIT :#{#pageSize} OFFSET :#{#offset}", nativeQuery = true)
    List<Review> findAllReviews(@Param("pageSize") int pageSize, @Param("offset") int offset);


    @Query(value = "SELECT CASE WHEN EXISTS ( SELECT 1 FROM reviews WHERE user_id = :userId AND review_date > CURRENT_DATE() - INTERVAL '7' DAY) THEN 'true' ELSE 'false' END AS has_reviewed;", nativeQuery = true)
    Boolean reviewInLastWeekForUser(@Param("userId") String userId);

    int countAllByCustomerFullNameStartingWith(String customerFullName);

    int countAllBy();

    Review findReviewByReviewId_ReviewIdAndUserId(String reviewId, String userId);

}
