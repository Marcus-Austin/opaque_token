package com.austin.opaque_token.entity;

import java.time.ZonedDateTime;

import org.hibernate.annotations.UuidGenerator;

import com.austin.opaque_token.common.ItemState;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class ItemEntity {

    @Id
    @UuidGenerator
    private String id;

    private String data;

    private ItemState itemState;

    private ZonedDateTime createdDate;

    private ZonedDateTime updatedDate;

  @PrePersist
  public void onPrePersist() {

    createdDate = ZonedDateTime.now();
    updatedDate = ZonedDateTime.now();
  }

  @PreUpdate
  public void onPreUpdate() {

    updatedDate = ZonedDateTime.now();
  }

}
