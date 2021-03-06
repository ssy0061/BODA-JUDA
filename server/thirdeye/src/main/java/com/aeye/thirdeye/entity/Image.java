package com.aeye.thirdeye.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Image")
@Getter @Setter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 200)
    private String image;

    @Size(max = 50)
    private String title;

    @Size(max = 1)
    private String imageValidate = "W";

    @Size(max = 20)
    private String typeA;

    @Size(max = 20)
    private String typeB;

    @Size(max = 20)
    private String typeC;

    @Size(max = 50)
    private String provider;

    private String faceYN;

    // 바운딩 박스 필요시 추가 예정
    private int L_X;

    private int L_Y;

    private int R_X;

    private int R_Y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Project project;
}

