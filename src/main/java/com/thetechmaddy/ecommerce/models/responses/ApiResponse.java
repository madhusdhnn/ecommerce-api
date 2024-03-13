package com.thetechmaddy.ecommerce.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.models.serializers.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thetechmaddy.ecommerce.models.responses.ApiResponseStatus.FAILED;
import static com.thetechmaddy.ecommerce.models.responses.ApiResponseStatus.SUCCESS;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @JsonSerialize(using = EnumSerializer.class)
    private ApiResponseStatus status;
    private T data;

    public ApiResponse(String status) {
        this.status = ApiResponseStatus.of(status);
        this.data = null;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>("success");
    }

    public static <V> ApiResponse<V> success(V data) {
        return new ApiResponse<>(SUCCESS, data);
    }

    public static <V> ApiResponse<V> error(V data) {
        return new ApiResponse<>(FAILED, data);
    }
}
