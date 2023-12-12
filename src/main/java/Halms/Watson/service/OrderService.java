package Halms.Watson.service;

import Halms.Watson.model.dto.ClientOrderDto;
import Halms.Watson.model.dto.Clients;
import Halms.Watson.model.dto.OrderDTO;
import Halms.Watson.model.entity.Orders;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    void createOrder(Clients clients) throws IOException;

    List<OrderDTO> getAllOrders();

    OrderDTO getOrder(Long id);

    void approveOrder(Long id, String userId);

    Orders completeOrder(Long id);

    List<ClientOrderDto> getAllOrdersByUser();

    void deleteByIdAndUserId(Long id, String userId);

    void deleteById(Long id);

    Optional<Orders> findById(Long id);

    void save(Orders orders);
}
