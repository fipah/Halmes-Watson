package Halms.Watson.repository;

import Halms.Watson.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("from Orders o where o.user.username = :username")
    List<Orders> findAllByUsername(String username);
}
