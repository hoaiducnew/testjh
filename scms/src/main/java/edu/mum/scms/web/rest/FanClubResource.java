package edu.mum.scms.web.rest;

import com.codahale.metrics.annotation.Timed;
import edu.mum.scms.domain.FanClub;

import edu.mum.scms.repository.FanClubRepository;
import edu.mum.scms.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FanClub.
 */
@RestController
@RequestMapping("/api")
public class FanClubResource {

    private final Logger log = LoggerFactory.getLogger(FanClubResource.class);

    private static final String ENTITY_NAME = "fanClub";

    private final FanClubRepository fanClubRepository;

    public FanClubResource(FanClubRepository fanClubRepository) {
        this.fanClubRepository = fanClubRepository;
    }

    /**
     * POST  /fan-clubs : Create a new fanClub.
     *
     * @param fanClub the fanClub to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fanClub, or with status 400 (Bad Request) if the fanClub has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fan-clubs")
    @Timed
    public ResponseEntity<FanClub> createFanClub(@RequestBody FanClub fanClub) throws URISyntaxException {
        log.debug("REST request to save FanClub : {}", fanClub);
        if (fanClub.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fanClub cannot already have an ID")).body(null);
        }
        FanClub result = fanClubRepository.save(fanClub);
        return ResponseEntity.created(new URI("/api/fan-clubs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fan-clubs : Updates an existing fanClub.
     *
     * @param fanClub the fanClub to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fanClub,
     * or with status 400 (Bad Request) if the fanClub is not valid,
     * or with status 500 (Internal Server Error) if the fanClub couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fan-clubs")
    @Timed
    public ResponseEntity<FanClub> updateFanClub(@RequestBody FanClub fanClub) throws URISyntaxException {
        log.debug("REST request to update FanClub : {}", fanClub);
        if (fanClub.getId() == null) {
            return createFanClub(fanClub);
        }
        FanClub result = fanClubRepository.save(fanClub);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fanClub.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fan-clubs : get all the fanClubs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fanClubs in body
     */
    @GetMapping("/fan-clubs")
    @Timed
    public List<FanClub> getAllFanClubs() {
        log.debug("REST request to get all FanClubs");
        return fanClubRepository.findAll();
    }

    /**
     * GET  /fan-clubs/:id : get the "id" fanClub.
     *
     * @param id the id of the fanClub to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fanClub, or with status 404 (Not Found)
     */
    @GetMapping("/fan-clubs/{id}")
    @Timed
    public ResponseEntity<FanClub> getFanClub(@PathVariable Long id) {
        log.debug("REST request to get FanClub : {}", id);
        FanClub fanClub = fanClubRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fanClub));
    }

    /**
     * DELETE  /fan-clubs/:id : delete the "id" fanClub.
     *
     * @param id the id of the fanClub to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fan-clubs/{id}")
    @Timed
    public ResponseEntity<Void> deleteFanClub(@PathVariable Long id) {
        log.debug("REST request to delete FanClub : {}", id);
        fanClubRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
