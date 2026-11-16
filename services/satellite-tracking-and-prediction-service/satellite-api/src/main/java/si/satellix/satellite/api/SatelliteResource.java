package si.satellix.satellite.api;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import si.satellix.satellite.api.generated.SatellitesApi;
import si.satellix.satellite.api.generated.model.SatelliteDetail;
import si.satellix.satellite.api.generated.model.SatelliteSummary;
import si.satellix.satellite.api.generated.model.SatellitesPage;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class SatelliteResource implements SatellitesApi {
    private List<SatelliteDetail> satellitesDetails;
    private List<SatelliteSummary> satellitesSummaries;

    @PostConstruct
    void loadSatelliteData(){
        SatelliteDetail iss = new SatelliteDetail();
        iss.setNoradId(25544);
        iss.setName("ISS (ZARYA)");
        iss.setInternationalDesignator("1998-067A");
        iss.setOwner("Demo owner");
        iss.setLaunchDate(LocalDate.of(1998, 11, 20));
        iss.setOrbitalPeriodMinutes(new BigDecimal("92.94"));

        SatelliteDetail hubble = new SatelliteDetail();
        hubble.setNoradId(20580);
        hubble.setName("HUBBLE");
        hubble.setInternationalDesignator("1990-037B");
        hubble.setOwner("Demo owner");
        hubble.setLaunchDate(LocalDate.of(1990, 4, 24));
        hubble.setOrbitalPeriodMinutes(new BigDecimal("95.40"));

        this.satellitesDetails = List.of(iss, hubble);

        this.satellitesSummaries = List.of(
                new SatelliteSummary(iss.getNoradId(), iss.getName()),
                new SatelliteSummary(hubble.getNoradId(), hubble.getName())
        );
    }

    @Override
    public SatelliteDetail getSatellite(Integer noradId) {
        for (SatelliteDetail satellite : satellitesDetails) {
            if(satellite.getNoradId().equals(noradId)) {
                return satellite;
            }
        }

        throw new NotFoundException("Satellite not found");
    }

    @Override
    public SatellitesPage listSatellites(Integer noradId, String name, Integer offset, Integer limit, String sort) {
        SatellitesPage page = new SatellitesPage();
        page.setItems(this.satellitesSummaries);
        page.setOffset(0);
        page.setLimit(20);
        page.setTotalItems(this.satellitesSummaries.size());
        page.setHasNext(false);
        page.setNextOffset(null);
        page.setCatalogSyncedAt(OffsetDateTime.now(Clock.systemUTC()));

        return page;
    }
}
