package com.lamthoncoding.realtimechat.payload.request;

import com.lamthoncoding.realtimechat.constraint.EGender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditProfileRequest {

    private String fullName;

    private String displayName;

    private String phone;

    private String avatar;

    private String timeZone;

    private LocalDate birthday;

    private EGender gender;
}