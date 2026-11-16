package si.satellix.satellite.api.error;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import si.satellix.satellite.api.generated.model.Problem;

@Provider // @Provider pove da naj quarkus oziroma JAX-RS (Jakarta rest) najde to svojo implementacijo (exception) mapperjev
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException exception) {
        Problem problem = new Problem();
        problem.setTitle("Bad Request");
        problem.setStatus(Response.Status.BAD_REQUEST.getStatusCode()); // 400
        problem.setDetail(exception.getMessage());

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type("application/problem+json")
                .entity(problem)
                .build();
    }
}
