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

    public enum Values {
        ADMIN("ADMIN"),
        BASIC("BASIC");

        private final String name;

        Values(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
