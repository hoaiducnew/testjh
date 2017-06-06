package edu.mum.scms.web.rest;

import edu.mum.scms.ScmsApp;

import edu.mum.scms.domain.FanClub;
import edu.mum.scms.repository.FanClubRepository;
import edu.mum.scms.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FanClubResource REST controller.
 *
 * @see FanClubResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScmsApp.class)
public class FanClubResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LEADER = "AAAAAAAAAA";
    private static final String UPDATED_LEADER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private FanClubRepository fanClubRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFanClubMockMvc;

    private FanClub fanClub;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FanClubResource fanClubResource = new FanClubResource(fanClubRepository);
        this.restFanClubMockMvc = MockMvcBuilders.standaloneSetup(fanClubResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FanClub createEntity(EntityManager em) {
        FanClub fanClub = new FanClub()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .leader(DEFAULT_LEADER)
            .address(DEFAULT_ADDRESS)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE);
        return fanClub;
    }

    @Before
    public void initTest() {
        fanClub = createEntity(em);
    }

    @Test
    @Transactional
    public void createFanClub() throws Exception {
        int databaseSizeBeforeCreate = fanClubRepository.findAll().size();

        // Create the FanClub
        restFanClubMockMvc.perform(post("/api/fan-clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fanClub)))
            .andExpect(status().isCreated());

        // Validate the FanClub in the database
        List<FanClub> fanClubList = fanClubRepository.findAll();
        assertThat(fanClubList).hasSize(databaseSizeBeforeCreate + 1);
        FanClub testFanClub = fanClubList.get(fanClubList.size() - 1);
        assertThat(testFanClub.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFanClub.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFanClub.getLeader()).isEqualTo(DEFAULT_LEADER);
        assertThat(testFanClub.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testFanClub.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFanClub.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createFanClubWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fanClubRepository.findAll().size();

        // Create the FanClub with an existing ID
        fanClub.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFanClubMockMvc.perform(post("/api/fan-clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fanClub)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FanClub> fanClubList = fanClubRepository.findAll();
        assertThat(fanClubList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFanClubs() throws Exception {
        // Initialize the database
        fanClubRepository.saveAndFlush(fanClub);

        // Get all the fanClubList
        restFanClubMockMvc.perform(get("/api/fan-clubs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fanClub.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].leader").value(hasItem(DEFAULT_LEADER.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getFanClub() throws Exception {
        // Initialize the database
        fanClubRepository.saveAndFlush(fanClub);

        // Get the fanClub
        restFanClubMockMvc.perform(get("/api/fan-clubs/{id}", fanClub.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fanClub.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.leader").value(DEFAULT_LEADER.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFanClub() throws Exception {
        // Get the fanClub
        restFanClubMockMvc.perform(get("/api/fan-clubs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFanClub() throws Exception {
        // Initialize the database
        fanClubRepository.saveAndFlush(fanClub);
        int databaseSizeBeforeUpdate = fanClubRepository.findAll().size();

        // Update the fanClub
        FanClub updatedFanClub = fanClubRepository.findOne(fanClub.getId());
        updatedFanClub
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .leader(UPDATED_LEADER)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE);

        restFanClubMockMvc.perform(put("/api/fan-clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFanClub)))
            .andExpect(status().isOk());

        // Validate the FanClub in the database
        List<FanClub> fanClubList = fanClubRepository.findAll();
        assertThat(fanClubList).hasSize(databaseSizeBeforeUpdate);
        FanClub testFanClub = fanClubList.get(fanClubList.size() - 1);
        assertThat(testFanClub.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFanClub.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFanClub.getLeader()).isEqualTo(UPDATED_LEADER);
        assertThat(testFanClub.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testFanClub.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFanClub.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingFanClub() throws Exception {
        int databaseSizeBeforeUpdate = fanClubRepository.findAll().size();

        // Create the FanClub

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFanClubMockMvc.perform(put("/api/fan-clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fanClub)))
            .andExpect(status().isCreated());

        // Validate the FanClub in the database
        List<FanClub> fanClubList = fanClubRepository.findAll();
        assertThat(fanClubList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFanClub() throws Exception {
        // Initialize the database
        fanClubRepository.saveAndFlush(fanClub);
        int databaseSizeBeforeDelete = fanClubRepository.findAll().size();

        // Get the fanClub
        restFanClubMockMvc.perform(delete("/api/fan-clubs/{id}", fanClub.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FanClub> fanClubList = fanClubRepository.findAll();
        assertThat(fanClubList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FanClub.class);
        FanClub fanClub1 = new FanClub();
        fanClub1.setId(1L);
        FanClub fanClub2 = new FanClub();
        fanClub2.setId(fanClub1.getId());
        assertThat(fanClub1).isEqualTo(fanClub2);
        fanClub2.setId(2L);
        assertThat(fanClub1).isNotEqualTo(fanClub2);
        fanClub1.setId(null);
        assertThat(fanClub1).isNotEqualTo(fanClub2);
    }
}
