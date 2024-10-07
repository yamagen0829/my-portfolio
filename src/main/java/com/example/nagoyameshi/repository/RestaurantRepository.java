package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);
    
	    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);
	    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable); 
	    public Page<Restaurant> findByAddressLikeOrderByCreatedAtDesc(String address, Pageable pageable);
	    public Page<Restaurant> findByAddressLikeOrderByPriceAsc(String address, Pageable pageable);
	    public Page<Restaurant> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
	    public Page<Restaurant> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);
	    public Page<Restaurant> findAllByOrderByCreatedAtDesc(Pageable pageable);
	    public Page<Restaurant> findAllByOrderByPriceAsc(Pageable pageable);    
	    
	    @Query("SELECT r FROM Restaurant r WHERE r.category.name LIKE %:category%")
	    Page<Restaurant> findByCategoryNameContaining(@Param("category") String category, Pageable pageable);
	    
	    public List<Restaurant> findTop5ByOrderByCreatedAtDesc();
	    
	    @Query("SELECT COUNT(r) FROM Restaurant r")
		Integer countAllRestaurants();
	    
	 // 新しい評価順のクエリ
	    @Query("SELECT r FROM Restaurant r LEFT JOIN Review rev ON r.id = rev.restaurant.id GROUP BY r.id ORDER BY AVG(rev.score) DESC")
	    Page<Restaurant> findAllByOrderByAverageScoreDesc(Pageable pageable);
	    
	    @Query("SELECT r FROM Restaurant r LEFT JOIN Review rev ON r.id = rev.restaurant.id WHERE r.name LIKE %:nameKeyword% OR r.address LIKE %:addressKeyword% GROUP BY r.id ORDER BY AVG(rev.score) DESC")
	    Page<Restaurant> findByNameOrAddressOrderByAverageScoreDesc(@Param("nameKeyword") String nameKeyword, @Param("addressKeyword") String addressKeyword, Pageable pageable);

	    @Query("SELECT r FROM Restaurant r LEFT JOIN Review rev ON r.id = rev.restaurant.id WHERE r.address LIKE %:address% GROUP BY r.id ORDER BY AVG(rev.score) DESC")
	    Page<Restaurant> findByAddressOrderByAverageScoreDesc(@Param("address") String address, Pageable pageable);

	    @Query("SELECT r FROM Restaurant r LEFT JOIN Review rev ON r.id = rev.restaurant.id WHERE r.price <= :price GROUP BY r.id ORDER BY AVG(rev.score) DESC")
	    Page<Restaurant> findByPriceOrderByAverageScoreDesc(@Param("price") Integer price, Pageable pageable);

	    @Query("SELECT r FROM Restaurant r LEFT JOIN Review rev ON r.id = rev.restaurant.id WHERE r.category.name LIKE %:category% GROUP BY r.id ORDER BY AVG(rev.score) DESC")
	    Page<Restaurant> findByCategoryNameOrderByAverageScoreDesc(@Param("category") String category, Pageable pageable);
	
}
