package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<Blog> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE FUNCTION('DATE', b.createdAt) = :date")
    Page<Blog> findByCreatedAt(@Param("date") LocalDate date, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE FUNCTION('DATE', b.updatedAt) = :date")
    Page<Blog> findByUpdatedAt(@Param("date") LocalDate date, Pageable pageable);
}
