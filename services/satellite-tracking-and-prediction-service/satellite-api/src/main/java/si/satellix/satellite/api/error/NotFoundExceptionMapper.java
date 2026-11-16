package si.satellix.satellite.api.error;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import si.satellix.satellite.api.generated.model.Problem;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        Response.Status status;
        String detail;

        if(exception.getCause() instanceof NumberFormatException) {
            // sem gre npr. /satellites?limit="abc"
            status = Response.Status.BAD_REQUEST;
            detail = "Number Format Exception in one or more request parameters";
        } else {
            // sem gre npr. /satellites/999999
            status = Response.Status.NOT_FOUND;
            // exception.getMessage() ima tudi custom sporocilo lahko ce naredis throw new NotFoundException("Satellite not found")
            detail = exception.getMessage();
        }

        Problem problem = new Problem();
        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(detail);
        problem.setStatus(status.getStatusCode());

        return Response
                .status(status)
                .type("application/problem+json")
                .entity(problem)
                .build();
    }
}
