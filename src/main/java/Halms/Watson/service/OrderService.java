package Halms.Watson.service;

import Halms.Watson.model.dto.Clients;
import Halms.Watson.model.dto.OrderDTO;
import Halms.Watson.model.entity.Orders;

import java.util.List;

public interface OrderService {

    void createOrder(Clients clients);

    List<OrderDTO> getAllOrders();

    OrderDTO getOrder(Long id);

    void approveOrder(Long id, Long userId);

    Orders completeOrder(Long id);

}
