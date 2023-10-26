package com.example.storyteller.entity;

import jakarta.persistence.*;
import lombok.Getter;

// Choose your inheritance strategy:
//@Inheritance(strategy=InheritanceType.JOINED)
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@Getter
@Entity
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
}