package com.example.ecommerce_api.controller.Order;

import java.nio.file.AccessDeniedException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_api.security.CustomerDetails;
import com.example.ecommerce_api.services.Order.ShipmentService;
import com.example.ecommerce_api.services.Order.ShipmentStatusDTO;

@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "http://localhost:4200")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<ShipmentStatusDTO> getShipmentStatus(
        @PathVariable Long orderId,
        @AuthenticationPrincipal CustomerDetails user
    ) throws AccessDeniedException {
        Long customerId = user.getCustomer().getUserId();
        ShipmentStatusDTO dto = shipmentService.getStatus(customerId, orderId);
        return ResponseEntity.ok(dto);
    }
}