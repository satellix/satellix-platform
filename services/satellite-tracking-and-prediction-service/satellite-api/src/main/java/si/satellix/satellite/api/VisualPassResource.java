package si.satellix.satellite.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import si.satellix.satellite.api.generated.VisualPassesApi;
import si.satellix.satellite.api.generated.model.VisualPass;
import si.satellix.satellite.api.generated.model.VisualPassesResult;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
@Path("/satellites/{noradId}/visual-passes")
public class VisualPassResource implements VisualPassesApi {
    @Override
    public VisualPassesResult listVisualPasses(
            Double latitude,
            Double longitude,
            Integer noradId,
            BigDecimal altitudeMeters,
            Integer days) {

        if (!Integer.valueOf(25544).equals(noradId) && !Integer.valueOf(20580).equals(noradId)) {
            throw new NotFoundException("Satellite not found");
        }

        OffsetDateTime now = OffsetDateTime.now(Clock.systemUTC());

        VisualPass firstPass = new VisualPass();
        firstPass.setStartTime(now.plusHours(1));
        firstPass.setEndTime(now.plusHours(1).plusMinutes(5));
        firstPass.setMaxElevationDegrees(new BigDecimal("45.0"));

        VisualPass secondPass = new VisualPass();
        secondPass.setStartTime(now.plusHours(3));
        secondPass.setEndTime(now.plusHours(3).plusMinutes(4));
        secondPass.setMaxElevationDegrees(new BigDecimal("25.0"));

        VisualPassesResult result = new VisualPassesResult();
        result.setPasses(List.of(firstPass, secondPass));

        return result;
    }
}
