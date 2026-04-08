package com.lamthoncoding.realtimechat.exception.handlers;

import lombok.Getter;

@Getter
public class InvalidInputException extends IllegalArgumentException {
    public InvalidInputException(String s) {
        super(s);
    }
}
