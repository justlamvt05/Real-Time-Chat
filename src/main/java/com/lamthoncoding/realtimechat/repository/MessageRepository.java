package com.lamthoncoding.realtimechat.repository;

import com.lamthoncoding.realtimechat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface MessageRepository extends JpaRepository<Message, UUID> {
}
