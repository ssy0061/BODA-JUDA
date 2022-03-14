package com.aeye.thirdeye.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "User")
@Getter
@Setter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 30)
    private String userId;

    @Size(max = 20)
    private String password;

    private String email;

    @Size(max = 20)
    private String nickName;

}