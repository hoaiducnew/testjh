package edu.mum.scms.repository;

import edu.mum.scms.domain.FanClub;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FanClub entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FanClubRepository extends JpaRepository<FanClub,Long> {

}
