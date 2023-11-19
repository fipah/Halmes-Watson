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

    @Query(value = """
    with completed_orders as (
    select id, price * 0.3 as reward
    from orders
    where
    employee_id = :employeeId
    and order_status_id = 3
    and extract(week from completed_date) = extract(week from now()))
    select case when count(*) > 2 then sum(reward) + (count(*)-2)*1000 else sum(reward) end from completed_orders
""", nativeQuery = true)
    Long countPaymentForLastSevenDaysWithBonuses(Long employeeId);
}
