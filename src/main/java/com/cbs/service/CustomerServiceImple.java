package com.cbs.service;

import com.cbs.dto.CustomerRequestDTO;
import com.cbs.dto.CustomerResponseDTO;
import com.cbs.entity.Customer;
import com.cbs.entity.KycStatus;
import com.cbs.exception.DublicateCustomerException;
import com.cbs.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImple implements CustomerService{
    private CustomerRepository customerRepository;
    public CustomerServiceImple(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
        if (request == null ||
                request.getEmail() == null || request.getEmail().isBlank() ||
                request.getName() == null || request.getName().isBlank() ||
                request.getPhone() == null || request.getPhone().isBlank()) {

            throw new IllegalArgumentException("Name, Email and Phone are required");
        }
        if (customerRepository.existsByEmail(request.getEmail()))
      {
          throw new DublicateCustomerException("Email already registered");
      }
      if (customerRepository.existsByPhone(request.getPhone()))
      {
          throw new DublicateCustomerException("Phone Already Registred");
      }
        Customer customer= new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setKycStatus(KycStatus.PENDING);
        customer.setPanNumber(request.getPanNumber());
        customer.setAddress(request.getAddress());
        customer.setAadhaarNumber(request.getAadhaarNumber());
        customerRepository.save(customer);
        CustomerResponseDTO responseDTO = new CustomerResponseDTO();
        responseDTO.setCustomerId(customer.getCustomerId());
        //responseDTO.setEmail(customer.getEmail());
        responseDTO.setName(customer.getName());
        //responseDTO.setPhone(customer.getPhone());
        responseDTO.setMessage("Customer Onboard successfully");
        return responseDTO;
    }

    @Override
    public List<CustomerResponseDTO> viewcustomer() {
        List<Customer> customer= customerRepository.findAll();
        List<CustomerResponseDTO>  list =  new ArrayList<>();
        for (Customer customer1 : customer)
       {
           CustomerResponseDTO responseDTO = new CustomerResponseDTO();
           responseDTO.setCustomerId(customer1.getCustomerId());
           responseDTO.setName(customer1.getName());
           responseDTO.setEmail(customer1.getEmail());
           responseDTO.setPhone(customer1.getPhone());
           responseDTO.setPanNumber(customer1.getPanNumber());
           responseDTO.setAddress(customer1.getAddress());
           responseDTO.setAadhaarNumber(customer1.getAadhaarNumber());
           responseDTO.setKycStatus(customer1.getKycStatus());
           list.add(responseDTO);
       }
        return list;
    }

    @Override
    public CustomerResponseDTO updateCustomer(CustomerRequestDTO requestDTO, Long custonerId) {
        Customer customer =customerRepository.findById(custonerId).orElseThrow(()-> new IllegalArgumentException("Customer not found"));
        if (customerRepository.existsByEmail(requestDTO.getEmail()))
        {
            throw new DublicateCustomerException("Email already registered");
        }
        if (customerRepository.existsByPhone(requestDTO.getPhone())) {
            throw new DublicateCustomerException("Phone already registered");
        }
        customer.setName(requestDTO.getName());
        customer.setPhone(requestDTO.getPhone());
        customer.setEmail(requestDTO.getEmail());
        customer.setPanNumber(requestDTO.getPanNumber());
        customer.setAddress(requestDTO.getAddress());
        customer.setAadhaarNumber(requestDTO.getAadhaarNumber());
       // customer.setKycStatus(KycStatus.PENDING);
        customerRepository.save(customer);
        CustomerResponseDTO responseDTO = new CustomerResponseDTO();
        responseDTO.setPhone(customer.getPhone());
        responseDTO.setName(customer.getName());
        responseDTO.setEmail(customer.getEmail());
       // responseDTO.setKycStatus(customer.getKycStatus());
        return responseDTO;
    }
}
