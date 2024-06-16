package com.project.shopapp.services.Impl;

import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.dtos.CartItemDTO;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.*;
import com.project.shopapp.response.order.OrderResponse;
import com.project.shopapp.repository.OrderDetailRepository;
import com.project.shopapp.repository.OrderRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSerivceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper  modelMapper;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
//        xem id có tồn tại ko
        User user=userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Ko tim thấy idUser with id:"+orderDTO.getUserId()));
//       convert orderDTO -> order
//        dùng thư viện ModelMapper
//        tạo một luồng ánh xạ riêng và bỏ trường ID vì request ko có ID
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper->mapper.skip(Order::setId));
//        lưu ý : ánh xạ giống tạo builder của Order và xét giá trị của OrderDTO(request) vào thực thể Order

        Order order=new Order();
        modelMapper.map(orderDTO,order);
        order.setUserId(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
//        kiểm tra shippingDate có >= thời gian hiện tại
//        su dung localDate lấy ngày tháng hiện tại ko có giờ giây
        LocalDate shippingDate=orderDTO.getShippingDate()==null? LocalDate.now() :orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("shipping date ko đc nhỏ hơn ngày hiện tại");
        }
        order.setActive(true);
        order.setShippingDate(shippingDate);
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.saveAndFlush(order);
        List<OrderDetail> orderDetails=new ArrayList<>();
        for(CartItemDTO cartItemDTO:orderDTO.getCartItemDTOS()){
            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setOrderId(order);

            Long productId=cartItemDTO.getProductId();
            int quantity=cartItemDTO.getQuantity();
//            tim thong tin san pham
            Product product=productRepository.findById(productId).
                    orElseThrow(() -> new DataNotFoundException("Ko tìm thấy Product with id "+productId));
//            set thông tin cho orderDetail
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setTotalMoney(product.getPrice()*quantity);
//            thêm 1 bản ghi orderDetail vào list orderDetails
            orderDetails.add(orderDetail);
        }
//        lưu list orderDetails vào database
        orderDetailRepository.saveAll(orderDetails);

        return order;
    }

    @Override
    public OrderResponse getOrderById(long id) {
        Order order=orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Ko tìm thấy idOrder:"+id));
        OrderResponse orderResponse=modelMapper.map(order,OrderResponse.class);
        return orderResponse;
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(long id, OrderDTO orderDTO) {
        Order order=orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Ko tìm thấy idOrder:"+id));
        User existingUser=userRepository.findById(orderDTO.getUserId()).orElseThrow(() ->
                new DataNotFoundException("Ko tìm thấy idOrder:"+orderDTO.getUserId()));

//   map từ orderDTO sang ORDER bỏ qua trường id vì ID tự tăng typeMap(kiểu map nhu TH dưới là bỏ id)
        modelMapper.typeMap(OrderDTO.class,Order.class).addMappings(mapper->mapper.skip(Order::setId));
        modelMapper.map(orderDTO,order);
        order.setUserId(existingUser);

        orderRepository.saveAndFlush(order);
        modelMapper.typeMap(Order.class,OrderResponse.class)
//        khi khách hàng chỉ cần lấy id của user chứ ko cần lấy đối tượng User
                .addMapping(src-> src.getUserId().getId(), OrderResponse::setUserId);
        OrderResponse orderResponse=new OrderResponse();
        modelMapper.map(order,orderResponse);
        return orderResponse;
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order=orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Ko tìm thấy idOrder:"+id));
        if(order!=null){
            order.setActive(false);
            orderRepository.saveAndFlush(order);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    @Override
    public Page<Order> getOrderByKeyword(String keyword, Pageable pageable){
        return orderRepository.findByKeyword(keyword,pageable);
    }
}
