package com.user.management.Models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Addresses {
    @Id
    private UUID id;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private int zip;
    private String country;
}
