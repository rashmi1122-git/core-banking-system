package com.cbs.service;

import com.cbs.dto.CustomerRequestDTO;
import com.cbs.dto.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {

  CustomerResponseDTO createCustomer(CustomerRequestDTO request);
  List<CustomerResponseDTO>  viewcustomer();
  CustomerResponseDTO updateCustomer(CustomerRequestDTO requestDTO,Long custonerId);


}
