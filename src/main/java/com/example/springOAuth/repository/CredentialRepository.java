package com.example.springOAuth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springOAuth.entity.Credential;
import com.example.springOAuth.entity.User;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    List<Credential> findByUser(User user);

}
