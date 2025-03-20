package vn.lqh.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.lqh.laptopshop.domain.Order;
import vn.lqh.laptopshop.domain.OrderDetail;
import vn.lqh.laptopshop.repository.CartDetailRepository;
import vn.lqh.laptopshop.service.OrderService;


@Controller
public class OrderController {

    private final CartDetailRepository cartDetailRepository;

    private final DaoAuthenticationProvider authProvider;

    private OrderService orderService;



    public OrderController(OrderService orderService, DaoAuthenticationProvider authProvider, CartDetailRepository cartDetailRepository) {
        this.orderService = orderService;
        this.authProvider = authProvider;
        this.cartDetailRepository = cartDetailRepository;
    }



    @GetMapping("/admin/order")
    public String getOrders(Model model,@RequestParam("page")Optional<String> pageOptional) {

        int page = 1;

        try {
            if(pageOptional.isPresent()){
                page = Integer.parseInt(pageOptional.get());
            }
            else{

            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page -1, 1);
        Page<Order> orders = this.orderService.getAllOrders(pageable);
        List<Order> listOrders = orders.getContent();
        model.addAttribute("orders", listOrders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        return "admin/order/show";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getOrderUpdate(Model model, @PathVariable long id) {
        Order currentOrder = this.orderService.fetchOrderById(id);
        model.addAttribute("newOrder", currentOrder);
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String postOrderUpdate(@ModelAttribute("newOrder") Order order) {
        Order currentOrder = this.orderService.fetchOrderById(order.getId());
        if (currentOrder != null) {
            currentOrder.setStatus(order.getStatus());
            this.orderService.updateOrder(currentOrder);
        }
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/detail/{id}")
    public String orderDetail(Model model, @PathVariable long id) {
        Order order = this.orderService.fetchOrderById(id);
        if(order != null){
            List<OrderDetail> detail = order.getOrderDetails();
            model.addAttribute("orderDetails", detail);
        }
        return "admin/order/detail";
    }

    
    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrder(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newOrder", new Order());
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String postMethodName(@ModelAttribute("newOrder") Order order) {
        this.orderService.deleteOrder(order.getId());
        return "redirect:/admin/order";
    }
    
    
    
    
    
}
