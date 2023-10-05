package Halms.Watson.service.impl;

import Halms.Watson.model.dto.Clients;
import Halms.Watson.model.dto.EmployeeDTO;
import Halms.Watson.model.dto.OrderDTO;
import Halms.Watson.model.entity.OrderStatus;
import Halms.Watson.model.entity.Orders;
import Halms.Watson.model.enums.OrderStatusEnum;
import Halms.Watson.repository.EmployeeRepository;
import Halms.Watson.repository.OrderRepository;
import Halms.Watson.repository.OrderStatusRepository;
import Halms.Watson.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void createOrder(Clients clients) {
        Orders orders = new Orders();
        OrderStatus statusByCode = orderStatusRepository.getStatusByCode(OrderStatusEnum.NEW);
        orders.setOrderStatus(statusByCode);
        orders.setClientName(clients.getName());
        orders.setPrice(clients.getPrice());
        orders.setDescription(clients.getDescription());
        orderRepository.save(orders);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderServiceImpl::convertOrderToDto)
                .collect(Collectors.toList());
    }

    private static OrderDTO convertOrderToDto(Orders orders) {
        if (Objects.isNull(orders)) {
            return null;
        }
        OrderDTO dto = new OrderDTO();
        dto.setId(orders.getId());
        dto.setDescription(orders.getDescription());
        dto.setPrice(orders.getPrice());
        dto.setStatus(orders.getOrderStatus().getCode());
        dto.setClientName(orders.getClientName());
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(orders.getEmployee().getId());
        employeeDTO.setName(orders.getEmployee().getName());
        employeeDTO.setAvatarUrl(orders.getEmployee().getAvatarUrl());
        dto.setAssignedEmployee(employeeDTO);
        dto.setCompletedDate(orders.getCompletedDate());
        dto.setCreatedDate(orders.getCreatedDate());
        return dto;
    }

    @Override
    public OrderDTO getOrder(Long id) {
        return convertOrderToDto(orderRepository.findById(id).orElse(null));
    }

    @Override
    public void approveOrder(Long id, Long userId) {
        Orders orders = orderRepository.findById(id).orElse(null);

    }

    @Override
    @Transactional
    public Orders completeOrder(Long id) {
        Orders orders = orderRepository.findById(id).orElse(null);
        if (Objects.isNull(orders)) {
            return orders;
        }
        OrderStatus statusByCode = orderStatusRepository.getStatusByCode(OrderStatusEnum.COMPLETED);
        orders.setOrderStatus(statusByCode);
        orders.setCompletedDate(OffsetDateTime.now());

        return orderRepository.save(orders);
    }
}
