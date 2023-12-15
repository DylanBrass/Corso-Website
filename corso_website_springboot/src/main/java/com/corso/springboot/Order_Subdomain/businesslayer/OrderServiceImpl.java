package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Order_Subdomain.datalayer.*;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderRequestMapper;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderResponseMapper;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestExternal;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
import com.corso.springboot.Service_Subdomain.businesslayer.ServiceService;
import com.corso.springboot.Service_Subdomain.businesslayer.ServiceServiceImpl;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderResponseMapper orderResponseMapper;
    private final OrderRequestMapper orderRequestMapper;

    private final ServiceService serviceService;

    @Override
    public List<OrderResponse> getAllOrdersByStatus(Map<String, String> queryParams, int pageSize, int offset) {

        if(queryParams.get("status") == null && pageSize == 0 && offset == 0){
            return orderResponseMapper.toOrdersResponse(orderRepository.findAll());
        }

        if(queryParams.get("status") == null){
            return orderResponseMapper.toOrdersResponse(orderRepository.findAllServices(pageSize, offset));
        }

        if(pageSize == 0 && offset == 0){
            Status statusEnum = Status.valueOf(queryParams.get("status").toUpperCase());
            return orderResponseMapper.toOrdersResponse(orderRepository.findAllByOrderStatusEquals(statusEnum));
        }

        Status statusEnum = Status.valueOf(queryParams.get("status").toUpperCase());
        return orderResponseMapper.toOrdersResponse(orderRepository.findAllByOrderStatusWithPagination(String.valueOf(statusEnum), pageSize, offset));
    }

    @Override
    public OrderResponse createOrderExternal(OrderRequestExternal orderRequestExternal) {

        if (serviceService.getServiceByServiceId(orderRequestExternal.getServiceId()) == null) {
            throw new RuntimeException("Service not found");
        }

        Order order = orderRequestMapper.requestModelToOrder(orderRequestExternal);

        order.setOrderId(new OrderIdentifier());

        order.setOrderTrackingNumber(new OrderTrackingNumber());

        order.setOrderStatus(Status.IN_PROGRESS);

        order.setServiceId(new ServiceIdentifier(orderRequestExternal.getServiceId()));

        order.setOrderDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        if(orderRequestExternal.getUserId() != null){
            order.setUserId(orderRequestExternal.getUserId());
        }else{
            order.setUserId("external_client");
        }



        return orderResponseMapper.toOrderResponse(orderRepository.save(order));
    }
}
