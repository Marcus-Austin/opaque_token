package com.austin.opaque_token.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.austin.opaque_token.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, String> {

    Page<ItemEntity> findByUserId(String userId, Pageable pageable);
}
