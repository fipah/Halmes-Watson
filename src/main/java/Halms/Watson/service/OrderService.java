package Halms.Watson.service;

import Halms.Watson.model.dto.Clients;
import Halms.Watson.model.dto.OrderDTO;
import Halms.Watson.model.entity.Orders;

import java.util.List;

public interface OrderService {

    void createOrder(Clients clients);

    List<OrderDTO> getAllOrders();

    OrderDTO getOrder(Long id);

    void approveOrder(Long id, String userId);

    Orders completeOrder(Long id);

    List<OrderDTO> getAllOrdersByUser();

    void deleteByIdAndUserId(Long id, String userId);

    void deleteById(Long id);
}
