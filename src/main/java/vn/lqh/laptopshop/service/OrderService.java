package vn.lqh.laptopshop.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.lqh.laptopshop.domain.Order;
import vn.lqh.laptopshop.domain.OrderDetail;
import vn.lqh.laptopshop.domain.User;
import vn.lqh.laptopshop.repository.OrderDetailRepository;
import vn.lqh.laptopshop.repository.OrderRepository;

@Service
public class OrderService {

    private final PasswordEncoder passwordEncoder;
    
    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

   

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, PasswordEncoder passwordEncoder) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Page<Order> getAllOrders(Pageable pageable){
         return this.orderRepository.findAll(pageable);
    }
    public Order updateOrder(Order order){
        return orderRepository.save(order);
    }

   

    public Order fetchOrderById(long id){
        return orderRepository.findById(id).get();
    }


    public void deleteOrder(long id){
        Order orderOptional = this.fetchOrderById(id);
        if (orderOptional != null) {
            Order order = orderOptional;
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails) {
                this.orderDetailRepository.deleteById(orderDetail.getId());
            }
        }

        this.orderRepository.deleteById(id);
    }
    public List<Order> fetchOrderByUser(User user) {
        return this.orderRepository.findByUser(user);
    }

}
