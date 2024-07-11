package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
//Load data from database and stored in the repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    @Query(
            value = "SELECT * FROM Users",
            nativeQuery = true)
    List<User> findAllUsers();
    User findUserByUserName(String username);
    User findByUserName(String userName);
    List<User> findByUserNameAndIdNot(String userName, int id);
    List<User> findByEmailAndIdNot(String email, int id);
    @Query("SELECT u FROM User u WHERE u.roleID.roleID != :roleID AND " +
            "(u.userName LIKE %:keyword% OR u.email LIKE %:keyword% OR u.fullName LIKE %:keyword% OR u.phoneNumber LIKE %:keyword%)")
    Page<User> searchUsersExcludingRole(
            @Param("roleID") Integer roleID,
            @Param("keyword") String keyword,
            Pageable pageable
    );
    Page<User> findByRoleID_RoleIDIsNot(Integer roleID, Pageable pageable);

}

