package com.cbs.controller;

import com.cbs.dto.CustomerRequestDTO;
import com.cbs.dto.CustomerResponseDTO;
import com.cbs.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @RequestBody CustomerRequestDTO request) {

        return ResponseEntity.ok(
                customerService.createCustomer(request)
        );
    }

    @GetMapping("/viewcustomer")
    public ResponseEntity<List<CustomerResponseDTO>> viewCustomer()
    {
        return ResponseEntity.ok(customerService.viewcustomer());

    }
    @PostMapping("/update/customer/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updatecustomer(@RequestBody CustomerRequestDTO dto,@PathVariable("customerId") Long customerId)
    {
        return ResponseEntity.ok(customerService.updateCustomer(dto, customerId));
    }
}
