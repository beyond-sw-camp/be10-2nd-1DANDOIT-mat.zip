package com.matzip.matzipuser.responsemessage;

import com.matzip.matzipuser.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResMessage {

    private int statusCode;
    private String success;
    private String message;

    public SuccessResMessage() {}

    public SuccessResMessage(SuccessCode successCode) {
        this.statusCode = successCode.getHttpStatus().value();
        this.success = successCode.getHttpStatus().name();
        this.message = successCode.getMessage();
    }

    public SuccessResMessage(SuccessCode successCode, String additionalMessage) {
        this.statusCode = successCode.getHttpStatus().value();
        this.success = successCode.getHttpStatus().name();
        this.message = additionalMessage; // 추가적인 메시지 사용
    }
}
