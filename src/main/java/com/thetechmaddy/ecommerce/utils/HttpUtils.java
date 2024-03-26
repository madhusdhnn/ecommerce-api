package com.thetechmaddy.ecommerce.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetechmaddy.ecommerce.exceptions.BusinessException;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.models.responses.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.Writer;

import static com.thetechmaddy.ecommerce.utils.JsonUtils.safeWriteAsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {

    public static void sendErrorResponse(ObjectMapper mapper, HttpServletResponse response, HttpStatus httpStatus, Throwable ex) {
        response.setStatus(httpStatus.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse("Something went wrong!");

        if (ex != null) {
            errorResponse.setMessage(ex.getMessage());

            if (ex instanceof BusinessException apiEx) {
                response.setStatus(apiEx.getStatus().value());
            }
        }

        try (Writer writer = response.getWriter()) {
            var apiResponse = ApiResponse.error(errorResponse);
            writer.write(safeWriteAsString(mapper, apiResponse));
        } catch (IOException ioEx) {
            throw new RuntimeException(ioEx);
        }
    }
}
