package com.paypal.bfs.test.employeeserv.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "employee")
@Builder
public class EmployeeDaoEntity {
    @Id
    private int id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private AddressDaoEntity addressDaoEntity;
}
