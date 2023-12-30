package com.afamo.iss.demo.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ResponseDto {
    private int httpStatus= HttpStatus.OK.value();
    private String message = "Successfull";
    private Object data;


}
