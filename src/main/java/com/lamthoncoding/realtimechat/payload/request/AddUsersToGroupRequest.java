package com.lamthoncoding.realtimechat.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AddUsersToGroupRequest {

    private List<UUID> userIds;
}