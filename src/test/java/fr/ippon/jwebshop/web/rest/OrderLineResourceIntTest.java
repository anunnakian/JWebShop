package fr.ippon.jwebshop.web.rest;

import fr.ippon.jwebshop.JWebShopApp;

import fr.ippon.jwebshop.domain.OrderLine;
import fr.ippon.jwebshop.repository.OrderLineRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrderLineResource REST controller.
 *
 * @see OrderLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JWebShopApp.class)
public class OrderLineResourceIntTest {

    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;

    @Inject
    private OrderLineRepository orderLineRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrderLineMockMvc;

    private OrderLine orderLine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderLineResource orderLineResource = new OrderLineResource();
        ReflectionTestUtils.setField(orderLineResource, "orderLineRepository", orderLineRepository);
        this.restOrderLineMockMvc = MockMvcBuilders.standaloneSetup(orderLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderLine createEntity(EntityManager em) {
        OrderLine orderLine = new OrderLine()
                .qty(DEFAULT_QTY);
        return orderLine;
    }

    @Before
    public void initTest() {
        orderLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderLine() throws Exception {
        int databaseSizeBeforeCreate = orderLineRepository.findAll().size();

        // Create the OrderLine

        restOrderLineMockMvc.perform(post("/api/order-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderLine)))
                .andExpect(status().isCreated());

        // Validate the OrderLine in the database
        List<OrderLine> orderLines = orderLineRepository.findAll();
        assertThat(orderLines).hasSize(databaseSizeBeforeCreate + 1);
        OrderLine testOrderLine = orderLines.get(orderLines.size() - 1);
        assertThat(testOrderLine.getQty()).isEqualTo(DEFAULT_QTY);
    }

    @Test
    @Transactional
    public void getAllOrderLines() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLines
        restOrderLineMockMvc.perform(get("/api/order-lines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderLine.getId().intValue())))
                .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)));
    }

    @Test
    @Transactional
    public void getOrderLine() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get the orderLine
        restOrderLineMockMvc.perform(get("/api/order-lines/{id}", orderLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderLine.getId().intValue()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY));
    }

    @Test
    @Transactional
    public void getNonExistingOrderLine() throws Exception {
        // Get the orderLine
        restOrderLineMockMvc.perform(get("/api/order-lines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderLine() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);
        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();

        // Update the orderLine
        OrderLine updatedOrderLine = orderLineRepository.findOne(orderLine.getId());
        updatedOrderLine
                .qty(UPDATED_QTY);

        restOrderLineMockMvc.perform(put("/api/order-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrderLine)))
                .andExpect(status().isOk());

        // Validate the OrderLine in the database
        List<OrderLine> orderLines = orderLineRepository.findAll();
        assertThat(orderLines).hasSize(databaseSizeBeforeUpdate);
        OrderLine testOrderLine = orderLines.get(orderLines.size() - 1);
        assertThat(testOrderLine.getQty()).isEqualTo(UPDATED_QTY);
    }

    @Test
    @Transactional
    public void deleteOrderLine() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);
        int databaseSizeBeforeDelete = orderLineRepository.findAll().size();

        // Get the orderLine
        restOrderLineMockMvc.perform(delete("/api/order-lines/{id}", orderLine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderLine> orderLines = orderLineRepository.findAll();
        assertThat(orderLines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
