package com.steverado.TeamWorkApi.repository;

import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO users (first_name, last_name, email, password, gender, job_role, department, address, created_at)
            VALUES (:firstName, :lastName, :email, :password, :gender, :jobRole, :department, :address, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void saveUser(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("password") String password,
            @Param("gender") String gender,
            @Param("jobRole") Role jobRole,
            @Param("department") String department,
            @Param("address")String address
    );

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

}
