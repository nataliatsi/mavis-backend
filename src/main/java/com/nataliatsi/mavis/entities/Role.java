package com.nataliatsi.mavis.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tb_roles")
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String name;


    public enum Values {
        ADMIN(1L),
        BASIC(2L);

        final Long roleId;

        Values(Long roleId){
            this.roleId = roleId;
        }


    }
}
