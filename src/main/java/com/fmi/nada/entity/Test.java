package com.fmi.nada.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="Nada")
@NoArgsConstructor
public class Test {

    public Test(String name){
        this.name = name;
    }
    @Id
    @GeneratedValue
    private Integer idx;

    private String name;
}
