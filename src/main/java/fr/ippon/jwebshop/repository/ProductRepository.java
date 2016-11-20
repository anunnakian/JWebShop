package fr.ippon.jwebshop.repository;

import fr.ippon.jwebshop.domain.Category;
import fr.ippon.jwebshop.domain.Product;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select a from Product a where a.category.id = :id")
    List<Product> findByCategory(@Param("id") long categoryId);
}
