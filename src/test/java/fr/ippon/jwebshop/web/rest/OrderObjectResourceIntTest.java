package fr.ippon.jwebshop.web.rest;

import fr.ippon.jwebshop.JWebShopApp;

import fr.ippon.jwebshop.domain.OrderObject;
import fr.ippon.jwebshop.domain.OrderLine;
import fr.ippon.jwebshop.repository.OrderObjectRepository;

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
 * Test class for the OrderObjectResource REST controller.
 *
 * @see OrderObjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JWebShopApp.class)
public class OrderObjectResourceIntTest {

    private static final Integer DEFAULT_ORDER_NUMBER = 1;
    private static final Integer UPDATED_ORDER_NUMBER = 2;

    @Inject
    private OrderObjectRepository orderObjectRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrderObjectMockMvc;

    private OrderObject orderObject;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderObjectResource orderObjectResource = new OrderObjectResource();
        ReflectionTestUtils.setField(orderObjectResource, "orderObjectRepository", orderObjectRepository);
        this.restOrderObjectMockMvc = MockMvcBuilders.standaloneSetup(orderObjectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderObject createEntity(EntityManager em) {
        OrderObject orderObject = new OrderObject()
                .orderNumber(DEFAULT_ORDER_NUMBER);
        // Add required entity
        OrderLine orderLine = OrderLineResourceIntTest.createEntity(em);
        em.persist(orderLine);
        em.flush();
        //orderObject.setOrderLine(orderLine);
        return orderObject;
    }

    @Before
    public void initTest() {
        orderObject = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderObject() throws Exception {
        int databaseSizeBeforeCreate = orderObjectRepository.findAll().size();

        // Create the OrderObject

        restOrderObjectMockMvc.perform(post("/api/order-objects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderObject)))
                .andExpect(status().isCreated());

        // Validate the OrderObject in the database
        List<OrderObject> orderObjects = orderObjectRepository.findAll();
        assertThat(orderObjects).hasSize(databaseSizeBeforeCreate + 1);
        OrderObject testOrderObject = orderObjects.get(orderObjects.size() - 1);
        assertThat(testOrderObject.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllOrderObjects() throws Exception {
        // Initialize the database
        orderObjectRepository.saveAndFlush(orderObject);

        // Get all the orderObjects
        restOrderObjectMockMvc.perform(get("/api/order-objects?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderObject.getId().intValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)));
    }

    @Test
    @Transactional
    public void getOrderObject() throws Exception {
        // Initialize the database
        orderObjectRepository.saveAndFlush(orderObject);

        // Get the orderObject
        restOrderObjectMockMvc.perform(get("/api/order-objects/{id}", orderObject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderObject.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingOrderObject() throws Exception {
        // Get the orderObject
        restOrderObjectMockMvc.perform(get("/api/order-objects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderObject() throws Exception {
        // Initialize the database
        orderObjectRepository.saveAndFlush(orderObject);
        int databaseSizeBeforeUpdate = orderObjectRepository.findAll().size();

        // Update the orderObject
        OrderObject updatedOrderObject = orderObjectRepository.findOne(orderObject.getId());
        updatedOrderObject
                .orderNumber(UPDATED_ORDER_NUMBER);

        restOrderObjectMockMvc.perform(put("/api/order-objects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrderObject)))
                .andExpect(status().isOk());

        // Validate the OrderObject in the database
        List<OrderObject> orderObjects = orderObjectRepository.findAll();
        assertThat(orderObjects).hasSize(databaseSizeBeforeUpdate);
        OrderObject testOrderObject = orderObjects.get(orderObjects.size() - 1);
        assertThat(testOrderObject.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void deleteOrderObject() throws Exception {
        // Initialize the database
        orderObjectRepository.saveAndFlush(orderObject);
        int databaseSizeBeforeDelete = orderObjectRepository.findAll().size();

        // Get the orderObject
        restOrderObjectMockMvc.perform(delete("/api/order-objects/{id}", orderObject.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderObject> orderObjects = orderObjectRepository.findAll();
        assertThat(orderObjects).hasSize(databaseSizeBeforeDelete - 1);
    }
}
