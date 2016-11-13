package fr.ippon.jwebshop.repository;

import fr.ippon.jwebshop.domain.OrderObject;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderObject entity.
 */
@SuppressWarnings("unused")
public interface OrderObjectRepository extends JpaRepository<OrderObject,Long> {

}
