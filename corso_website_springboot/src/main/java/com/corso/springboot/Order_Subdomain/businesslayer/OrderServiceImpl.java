package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Order_Subdomain.datalayer.OrderRepository;
import com.corso.springboot.Order_Subdomain.datalayer.Status;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderResponseMapper;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderResponseMapper orderResponseMapper;

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
}
