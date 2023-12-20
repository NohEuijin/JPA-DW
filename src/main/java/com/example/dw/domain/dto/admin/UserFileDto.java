package com.example.dw.domain.dto.admin;


import com.example.dw.domain.entity.user.Users;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class UserFileDto {
    private Long id;
    private String route;
    private String name;
    private String uuid;
    private Long userid;


    @QueryProjection
    public UserFileDto(Long id, String route, String name, String uuid, Long userid) {
        this.id = id;
        this.route = route;
        this.name = name;
        this.uuid = uuid;
        this.userid = userid;
    }
}
