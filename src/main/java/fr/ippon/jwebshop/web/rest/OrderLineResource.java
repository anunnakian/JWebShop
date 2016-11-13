package fr.ippon.jwebshop.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ippon.jwebshop.domain.OrderLine;

import fr.ippon.jwebshop.repository.OrderLineRepository;
import fr.ippon.jwebshop.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OrderLine.
 */
@RestController
@RequestMapping("/api")
public class OrderLineResource {

    private final Logger log = LoggerFactory.getLogger(OrderLineResource.class);
        
    @Inject
    private OrderLineRepository orderLineRepository;

    /**
     * POST  /order-lines : Create a new orderLine.
     *
     * @param orderLine the orderLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderLine, or with status 400 (Bad Request) if the orderLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-lines")
    @Timed
    public ResponseEntity<OrderLine> createOrderLine(@RequestBody OrderLine orderLine) throws URISyntaxException {
        log.debug("REST request to save OrderLine : {}", orderLine);
        if (orderLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderLine", "idexists", "A new orderLine cannot already have an ID")).body(null);
        }
        OrderLine result = orderLineRepository.save(orderLine);
        return ResponseEntity.created(new URI("/api/order-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-lines : Updates an existing orderLine.
     *
     * @param orderLine the orderLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderLine,
     * or with status 400 (Bad Request) if the orderLine is not valid,
     * or with status 500 (Internal Server Error) if the orderLine couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-lines")
    @Timed
    public ResponseEntity<OrderLine> updateOrderLine(@RequestBody OrderLine orderLine) throws URISyntaxException {
        log.debug("REST request to update OrderLine : {}", orderLine);
        if (orderLine.getId() == null) {
            return createOrderLine(orderLine);
        }
        OrderLine result = orderLineRepository.save(orderLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderLine", orderLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-lines : get all the orderLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orderLines in body
     */
    @GetMapping("/order-lines")
    @Timed
    public List<OrderLine> getAllOrderLines() {
        log.debug("REST request to get all OrderLines");
        List<OrderLine> orderLines = orderLineRepository.findAll();
        return orderLines;
    }

    /**
     * GET  /order-lines/:id : get the "id" orderLine.
     *
     * @param id the id of the orderLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderLine, or with status 404 (Not Found)
     */
    @GetMapping("/order-lines/{id}")
    @Timed
    public ResponseEntity<OrderLine> getOrderLine(@PathVariable Long id) {
        log.debug("REST request to get OrderLine : {}", id);
        OrderLine orderLine = orderLineRepository.findOne(id);
        return Optional.ofNullable(orderLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-lines/:id : delete the "id" orderLine.
     *
     * @param id the id of the orderLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderLine(@PathVariable Long id) {
        log.debug("REST request to delete OrderLine : {}", id);
        orderLineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderLine", id.toString())).build();
    }

}
