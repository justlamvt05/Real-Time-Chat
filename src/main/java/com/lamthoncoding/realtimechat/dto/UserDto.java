package com.lamthoncoding.realtimechat.dto;


import com.lamthoncoding.realtimechat.constraint.EStatus;
import lombok.*;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private EStatus status;

}
