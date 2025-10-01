package com.nataliatsi.mavis.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Table(name = "tb_roles")
@Data
public class Role {
    @Id
    @Column(name = "role_id")
    private Long roleId;
    private String name;

    public enum Values {
        ADMIN(1L),
        BASIC(2L);

        final long roleId;

        Values(long roleId){
            this.roleId = roleId;
        }

        public String getName() {
            return name();
        }

        public Long getRoleId() {
            return roleId;
        }
    }
}
