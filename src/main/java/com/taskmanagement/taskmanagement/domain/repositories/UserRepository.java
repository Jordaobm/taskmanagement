package com.taskmanagement.taskmanagement.domain.repositories;

import com.taskmanagement.taskmanagement.domain.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByEmail(String email);

}
