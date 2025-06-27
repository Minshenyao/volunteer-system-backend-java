package edu.ahut.volunteersystembackend.dao;

import edu.ahut.volunteersystembackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根据邮件查找指定用户
     */
    User findByEmail(String email);
}
