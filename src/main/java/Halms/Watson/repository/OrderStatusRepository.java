package Halms.Watson.repository;

import Halms.Watson.model.entity.OrderStatus;
import Halms.Watson.model.enums.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    @Query("select os from OrderStatus os where os.code = :code")
    OrderStatus getStatusByCode(OrderStatusEnum code);

    OrderStatus getByCode(OrderStatusEnum orderStatusEnum);
}
