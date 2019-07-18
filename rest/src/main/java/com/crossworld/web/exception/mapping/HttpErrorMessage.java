package com.crossworld.web.exception.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpErrorMessage {

    private int errorCode;
    private String errorMessage;
    private String errorDetails;
}
