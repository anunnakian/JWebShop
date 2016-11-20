package fr.ippon.jwebshop.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ippon.jwebshop.domain.OrderLine;
import fr.ippon.jwebshop.domain.OrderObject;

import fr.ippon.jwebshop.domain.Product;
import fr.ippon.jwebshop.repository.OrderLineRepository;
import fr.ippon.jwebshop.repository.OrderObjectRepository;
import fr.ippon.jwebshop.repository.ProductRepository;
import fr.ippon.jwebshop.repository.UserRepository;
import fr.ippon.jwebshop.security.AuthoritiesConstants;
import fr.ippon.jwebshop.security.SecurityUtils;
import fr.ippon.jwebshop.service.dto.CartDTO;
import fr.ippon.jwebshop.service.dto.CartItemDTO;
import fr.ippon.jwebshop.web.rest.util.HeaderUtil;
import fr.ippon.jwebshop.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OrderObject.
 */
@RestController
@RequestMapping("/api")
public class OrderObjectResource {

    private final Logger log = LoggerFactory.getLogger(OrderObjectResource.class);

    @Inject
    private OrderObjectRepository orderObjectRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private OrderLineRepository orderLineRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /order-objects : Create a new orderObject.
     *
     * @param orderObject the orderObject to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderObject, or with status 400 (Bad Request) if the orderObject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-objects")
    @Timed
    public ResponseEntity<OrderObject> createOrderObject(@Valid @RequestBody OrderObject orderObject) throws URISyntaxException {
        log.debug("REST request to save OrderObject : {}", orderObject);
        if (orderObject.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderObject", "idexists", "A new orderObject cannot already have an ID")).body(null);
        }
        OrderObject result = orderObjectRepository.save(orderObject);
        return ResponseEntity.created(new URI("/api/order-objects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderObject", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-objects : Updates an existing orderObject.
     *
     * @param orderObject the orderObject to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderObject,
     * or with status 400 (Bad Request) if the orderObject is not valid,
     * or with status 500 (Internal Server Error) if the orderObject couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-objects")
    @Timed
    public ResponseEntity<OrderObject> updateOrderObject(@Valid @RequestBody OrderObject orderObject) throws URISyntaxException {
        log.debug("REST request to update OrderObject : {}", orderObject);
        if (orderObject.getId() == null) {
            return createOrderObject(orderObject);
        }
        OrderObject result = orderObjectRepository.save(orderObject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderObject", orderObject.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-objects : get all the orderObjects.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orderObjects in body
     */
    @GetMapping("/order-objects")
    @Timed
    public List<OrderObject> getAllOrderObjects() {
        log.debug("REST request to get all OrderObjects");
        List<OrderObject> orderObjects = orderObjectRepository.findAll();
        return orderObjects;
    }

    /**
     * GET  /order-objects/:id : get the "id" orderObject.
     *
     * @param id the id of the orderObject to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderObject, or with status 404 (Not Found)
     */
    @GetMapping("/order-objects/{id}")
    @Timed
    public ResponseEntity<OrderObject> getOrderObject(@PathVariable Long id) {
        log.debug("REST request to get OrderObject : {}", id);
        OrderObject orderObject = orderObjectRepository.findOne(id);
        return Optional.ofNullable(orderObject)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-objects/:id : delete the "id" orderObject.
     *
     * @param id the id of the orderObject to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-objects/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderObject(@PathVariable Long id) {
        log.debug("REST request to delete OrderObject : {}", id);
        orderObjectRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderObject", id.toString())).build();
    }

    /**
     * POST  /checkout : Create a new order from a cart.
     *
     * @param cart the Order to create.
     * @return the ResponseEntity with status 201 (Created) and with body the new orderObject, or with status 400 (Bad Request) if the orderObject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/checkout")
    @Timed
    public ResponseEntity<OrderObject> checkout(@Valid @RequestBody CartDTO cart) throws URISyntaxException {
        log.debug("REST request to save OrderObject : {}", cart);
        OrderObject orderObject = new OrderObject();
        orderObject.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        OrderObject result = orderObjectRepository.save(orderObject);
        for (CartItemDTO item : cart.getItems()) {
            OrderLine line = new OrderLine();
            Product p = productRepository.findOne(item.getId());
            line.setProduct(p);
            line.setQty(item.getQuantity());
            line.setOrderObject(result);
            orderLineRepository.save(line);
            log.debug("REST request to save OrderLine : {}", line);
        }
        return ResponseEntity.created(new URI("/api/checkout/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderObject", result.getId().toString()))
            .body(result);
    }
}
