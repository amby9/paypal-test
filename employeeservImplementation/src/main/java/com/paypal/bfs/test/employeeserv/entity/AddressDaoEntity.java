package com.paypal.bfs.test.employeeserv.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class AddressDaoEntity {
    @Id
    private int id;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
