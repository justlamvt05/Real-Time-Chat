package com.lamthoncoding.realtimechat.dto;


import com.lamthoncoding.realtimechat.constraint.EStatus;
import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private EStatus status;

}
