package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class SoftDelete {

    @Column(nullable = false)
    private boolean deleted = false;
}
