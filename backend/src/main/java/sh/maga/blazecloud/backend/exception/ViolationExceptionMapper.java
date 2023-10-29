package sh.maga.blazecloud.backend.exception;

import jakarta.annotation.Priority;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Provider
@Priority(2)
public class ViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String field = null;
            for(Path.Node node : violation.getPropertyPath()) {
                field = node.getName();
            }
            if(field != null) {
                errors.add(field + " " + violation.getMessage());
            }
        }
        StringJoiner joiner = new StringJoiner("; ");
        for (String error : errors) {
            joiner.add(error);
        }
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorResponse(Response.Status.CONFLICT.getStatusCode(), joiner.toString())).build();
    }
}