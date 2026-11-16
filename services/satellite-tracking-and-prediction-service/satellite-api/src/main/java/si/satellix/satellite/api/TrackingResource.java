package si.satellix.satellite.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import si.satellix.satellite.api.generated.TrackingApi;
import si.satellix.satellite.api.generated.model.GeographicPosition;
import si.satellix.satellite.api.generated.model.SatellitePositions;
import si.satellix.satellite.api.generated.model.TrajectoryPoint;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
@Path("/satellites/{noradId}/positions")
public class TrackingResource implements TrackingApi {
    @Override
    public SatellitePositions getSatellitePositions(Integer noradId) {
        if (!Integer.valueOf(25544).equals(noradId) && !Integer.valueOf(20580).equals(noradId)) {
            throw new NotFoundException("Satellite not found");
        }

        GeographicPosition currentPosition = new GeographicPosition();
        currentPosition.setTimestamp(OffsetDateTime.now(Clock.systemUTC()));
        currentPosition.setLatitude(new BigDecimal("46.05"));
        currentPosition.setLongitude(new BigDecimal("14.51"));
        currentPosition.setAltitudeKm(new BigDecimal("420.00"));

        // 10 sekund po current position
        TrajectoryPoint firstPoint = new TrajectoryPoint();
        firstPoint.setLatitude(new BigDecimal("46.30"));
        firstPoint.setLongitude(new BigDecimal("15.20"));

        // 20 sekund po current position
        TrajectoryPoint secondPoint = new TrajectoryPoint();
        secondPoint.setLatitude(new BigDecimal("46.55"));
        secondPoint.setLongitude(new BigDecimal("15.90"));

        SatellitePositions positions = new SatellitePositions();
        positions.setNoradId(noradId);
        positions.setCurrentPosition(currentPosition);
        positions.setTrajectory(List.of(firstPoint, secondPoint));

        return positions;
    }
}
