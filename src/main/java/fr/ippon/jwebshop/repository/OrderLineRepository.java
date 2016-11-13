package fr.ippon.jwebshop.repository;

import fr.ippon.jwebshop.domain.OrderLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderLine entity.
 */
@SuppressWarnings("unused")
public interface OrderLineRepository extends JpaRepository<OrderLine,Long> {

}
