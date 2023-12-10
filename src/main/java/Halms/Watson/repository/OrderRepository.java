package Halms.Watson.repository;

import Halms.Watson.model.entity.Orders;
import Halms.Watson.model.enums.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("from Orders o where o.user.username = :username")
    List<Orders> findAllByUsername(String username);

    @Query(value = """
                with user_id as (select id from users where username = :username),
                                completed_orders as (
                                select o.id, s.price * 0.3 as reward
                                from orders o
                                join service s on s.id = o.service_id
                                where
                                employee_id = (select id from user_id)
                                and order_status_id = 3
                                and extract(week from completed_date) = extract(week from now()))
                                select case when count(*) > 2 then sum(reward) + (count(*)-2)*1000 else sum(reward) end from completed_orders
            """, nativeQuery = true)
    Long countPaymentForLastSevenDaysWithBonuses(String username);

    Orders findByIdAndUserUsername(Long orderId, String userId);

    @Query(value = """
    with employee as (select id from users where username = :username),
    order_status as (select id from order_status where code = :orderStatusEnum)
    select count(*) from orders where employee_id = (select id from employee) and order_status_id = (select id from order_status)
""", nativeQuery = true)
    Long getCountByStatusAndEmployeeUsername(String orderStatusEnum, String username);

    @Query("from Orders o where o.orderStatus.code = :orderStatusEnum")
    List<Orders> getByStatus(String orderStatusEnum);
}
