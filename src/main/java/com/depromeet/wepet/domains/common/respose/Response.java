package com.depromeet.wepet.domains.common.respose;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private Object data;
    private String message;

    public static Response of(Object object) {
        return Response
                .builder()
                .message("success")
                .data(object)
                .build();
    }
}
