package com.fmi.nada.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "Nada")
@NoArgsConstructor
public class Test {

    @Id
    @GeneratedValue
    private Integer idx;

    private String name;

    public Test(String name) {
        this.name = name;
    }
}
