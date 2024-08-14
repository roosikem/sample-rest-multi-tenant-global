package com.lkup.accounts.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDto {

    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
}
