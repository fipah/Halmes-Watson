package Halms.Watson.service.impl;

import Halms.Watson.constants.ContentTypeConsts;
import Halms.Watson.model.dto.ClientOrderDto;
import Halms.Watson.model.dto.Clients;
import Halms.Watson.model.dto.EmployeeDTO;
import Halms.Watson.model.dto.OrderDTO;
import Halms.Watson.model.entity.OrderStatus;
import Halms.Watson.model.entity.Orders;
import Halms.Watson.model.entity.Profile;
import Halms.Watson.model.entity.Users;
import Halms.Watson.model.enums.OrderStatusEnum;
import Halms.Watson.repository.*;
import Halms.Watson.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProfileRepository profileRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    @Override
    @Transactional
    public void createOrder(Clients clients) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        Optional<Users> byUsername = userRepository.findByUsername(username);
        Users users = byUsername.get();
        Orders orders = new Orders();
        orders.setUser(users);
        System.out.println(clients.getService());
        Halms.Watson.model.entity.Service service = serviceRepository.findByName(clients.getService());
        orders.setService(service);
        OrderStatus statusByCode = orderStatusRepository.getStatusByCode(OrderStatusEnum.PROCESSING);
        orders.setOrderStatus(statusByCode);
        orders.setClientName(users.getName());
        orders.setPrice(null);
        orders.setDescription(clients.getDescription());
        MultipartFile confirmationPhoto = clients.getConfirmationPhoto();
        if (Objects.nonNull(confirmationPhoto)) {
            String contentType = confirmationPhoto.getContentType();
            if (ContentTypeConsts.allowedContentTypes.contains(contentType)) {
                orders.setConfirmationPhoto(confirmationPhoto.getBytes());
                orders.setConfirmationPhotoContentType(contentType);
            }

        }
        orderRepository.save(orders);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(this::convertOrderToDto).collect(Collectors.toList());
    }

    private OrderDTO convertOrderToDto(Orders orders) {
        if (Objects.isNull(orders)) {
            return null;
        }
        OrderDTO dto = new OrderDTO();
        dto.setId(orders.getId());
        dto.setDescription(orders.getDescription());
        dto.setPrice(orders.getService().getPrice());
        dto.setService(orders.getService().getName());
        dto.setStatus(orders.getOrderStatus().getCode());
        Long userId = orders.getUser().getId();
        dto.setClientName(profileRepository.findById(userId).orElse(emptyProfile()).getFullName());
        EmployeeDTO employeeDTO = new EmployeeDTO();
        if (Objects.nonNull(orders.getEmployee())) {
            employeeDTO.setId(orders.getEmployee().getId());
            employeeDTO.setName(orders.getEmployee().getName());

            dto.setAssignedEmployee(employeeDTO);
        }
        dto.setCompletedDate(orders.getCompletedDate());
        dto.setCreatedDate(orders.getCreatedDate());
        return dto;
    }

    private Profile emptyProfile() {
        Profile profile = new Profile();
        profile.setFullName("");
        return profile;
    }

    @Override
    public OrderDTO getOrder(Long id) {
        return convertOrderToDto(orderRepository.findById(id).orElse(null));
    }

    @Override
    public void approveOrder(Long id, String userId) {
        Orders orders = orderRepository.findById(id).orElse(null);
        if (Objects.isNull(orders)) {
            return;
        }
        OrderStatus statusByCode = orderStatusRepository.getStatusByCode(OrderStatusEnum.IN_PROGRESS);
        orders.setOrderStatus(statusByCode);
        Optional<Users> byId = userRepository.findByUsername(userId);
        orders.setEmployee(byId.get());

        orderRepository.save(orders);
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

    @Override
    public List<ClientOrderDto> getAllOrdersByUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        return orderRepository.findAllByUsername(username).stream().map(this::convertToClientOrderDto).collect(Collectors.toList());
    }

    private ClientOrderDto convertToClientOrderDto(Orders orders) {
        if (Objects.isNull(orders)) {
            return null;
        }
        ClientOrderDto dto = new ClientOrderDto();
        dto.setId(orders.getId());
        dto.setDescription(orders.getDescription());
        dto.setPrice(orders.getService().getPrice());
        dto.setService(orders.getService().getName());
        dto.setStatus(orders.getOrderStatus().getCode());
        Long userId = orders.getUser().getId();
        dto.setClientName(profileRepository.findById(userId).orElse(emptyProfile()).getFullName());
        EmployeeDTO employeeDTO = new EmployeeDTO();
        if (Objects.nonNull(orders.getEmployee())) {
            employeeDTO.setId(orders.getEmployee().getId());
            employeeDTO.setName(orders.getEmployee().getName());

            dto.setAssignedEmployee(employeeDTO);
        }
        dto.setCompletedDate(orders.getCompletedDate());
        dto.setCreatedDate(orders.getCreatedDate());
        if (Objects.nonNull(orders.getEmployee())){
            Long id = orders.getEmployee().getId();
            dto.setEmployeeName(profileRepository.findById(id).orElse(emptyProfile()).getFullName());
        }
        return dto;
    }

    @Override
    public void deleteByIdAndUserId(Long orderId, String userId) {
        Orders order = orderRepository.findByIdAndUserUsername(orderId, userId);
        if (Objects.isNull(order) ||
                order.getOrderStatus().getCode().equals(OrderStatusEnum.COMPLETED) ||
                order.getOrderStatus().getCode().equals(OrderStatusEnum.IN_PROGRESS)) {
            return;
        }

        orderRepository.delete(order);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Optional<Orders> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void save(Orders orders) {
        orderRepository.save(orders);
    }
}
