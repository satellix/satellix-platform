/*
 * ExceptionMapper skrbi za pretvorbo izjeme v HTTP odgovor
 * \-> na primer pri /satellites parameter limit po openapi specifikaciji zavzema vrednost iz iztervala [1,100]
 *     in npr. limit=0 napako ujame Bean validation ampak npr. limit="abc" pa moramo ujeti mi saj je nemogoce pretvoriti
 *     string v Integer
 */

package si.satellix.satellite.api.error;

import io.quarkus.hibernate.validator.runtime.jaxrs.ResteasyReactiveViolationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import si.satellix.satellite.api.generated.model.FieldError;
import si.satellix.satellite.api.generated.model.Problem;

/*
 * ResteasyReactiveViolationException -> wrapper okoli jakarta.validation.ConstraintViolationException
 *
 * uporablja se za constraint violatione za REST endpoint klic (interne napake vracajo kode 500+ medtem ko napacni parametri
 * v requestu vracajo kode 400 (client error))
 */
//
@Provider // @Provider pove da naj quarkus oziroma JAX-RS (Jakarta rest) najde to svojo implementacijo (exception) mapperjev
public class RequestValidationExceptionMapper implements ExceptionMapper<ResteasyReactiveViolationException> {
    @Override
    public Response toResponse(ResteasyReactiveViolationException exception) {
        Problem problem = new Problem();
        problem.setTitle("Bad Request");
        problem.setStatus(Response.Status.BAD_REQUEST.getStatusCode()); // 400
        problem.setDetail("One or more request parameters are invalid");

        // ConstraintViolation-i so napaki ki se ticejo constraintov kot jih poda openapi specifikacija
        // ce pogledas target/.../api/generated/SatellitesApi.java vidis vse ki se pretičejo /satellites in /satellites/{noradId}
        // #DISCLOSURE:AI
        for(ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String methodName = null;
            String parameterName = null;

            for (Path.Node node : violation.getPropertyPath()) {
                // napaka v vrnjeni vrednosti je napaka streznika ne pa napaka uporabnika
                if (node.getKind() == ElementKind.RETURN_VALUE) {
                    throw exception;
                }

                if (node.getKind() == ElementKind.METHOD) {
                    methodName = node.getName();
                }
                if (node.getKind() == ElementKind.PARAMETER) {
                    parameterName = node.getName();
                }
            }

            if (parameterName != null){
                // 16.11.2026: po trenutni openapi specifikaciji je noradId query parameter samo pri /satellites za potrebe filtriranja/sortiranja
                boolean isPathParameter = "noradId".equals(parameterName) && !"listSatellites".equals(methodName);
                String prefix = isPathParameter ? "#/path/" : "#/query/";
                problem.addErrorsItem(
                        new FieldError(prefix + parameterName, violation.getMessage())
                );
            }
        }

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type("application/problem+json")
                .entity(problem)
                .build();
    }
}
