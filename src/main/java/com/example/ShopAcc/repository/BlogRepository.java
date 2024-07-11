package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    List<Blog> findByNameContaining(String name);
    List<Blog> findByCreatedAt(Date Date);
    //List<Blog> findByUpdatedAtBetween(Date startDate, Date endDate);
}
