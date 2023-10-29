package sh.maga.blazecloud.backend.exception;

import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Provider
@Priority(1)
public class BaseExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        if(exception instanceof ServiceException service) {
            return Response.status(service.getStatus()).entity(new ErrorResponse(service.getStatus(), service.getMessage())).build();
        }
        return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse(Response.Status.CONFLICT.getStatusCode(), exception.getMessage())).build();
    }
}
