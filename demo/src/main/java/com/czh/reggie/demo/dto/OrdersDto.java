package com.czh.reggie.demo.dto;

import com.czh.reggie.demo.pojo.OrderDetail;
import com.czh.reggie.demo.pojo.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
