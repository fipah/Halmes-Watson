package Halms.Watson.controller;

import Halms.Watson.model.dto.EmployeePaymentDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/v1/payments")
public class PaymentController {

    @GetMapping("/employee/")
    public EmployeePaymentDTO getTotalPayment(LocalDate date) {
        return null;
    }


}
