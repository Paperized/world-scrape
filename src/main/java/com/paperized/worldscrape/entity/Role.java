package com.paperized.worldscrape.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(name = "uc_role_name", columnNames = "name"))
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
}
