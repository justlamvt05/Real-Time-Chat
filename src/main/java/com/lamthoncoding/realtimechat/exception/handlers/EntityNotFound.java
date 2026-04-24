package com.lamthoncoding.realtimechat.exception.handlers;

import lombok.Getter;

@Getter
public class EntityNotFound extends RuntimeException {
    public EntityNotFound(String msg) {
        super(msg);
    }
}
