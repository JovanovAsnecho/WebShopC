package com.example.webshop.Model;

import javax.persistence.*;

@Entity
@Table(name = "contact")
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    public String location;

    public String firstName;

    public String lastName;

    public String phone;
}
