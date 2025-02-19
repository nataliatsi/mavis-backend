package com.nataliatsi.mavis.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Table(name = "tb_roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String name;

    @Getter
    public enum Values {
        ADMIN(1L),
        BASIC(2L);

        final long roleId;

        Values(long roleId){
            this.roleId = roleId;
        }


    }
}
