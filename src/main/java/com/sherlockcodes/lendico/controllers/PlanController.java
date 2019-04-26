package com.sherlockcodes.lendico.controllers;

import com.sherlockcodes.lendico.models.PaymentDTO;
import com.sherlockcodes.lendico.models.PaymentInput;
import com.sherlockcodes.lendico.models.PaymentPlan;
import com.sherlockcodes.lendico.service.PlanGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plan")
@Api(value = "plan enter", description = "basic controller that lets users create payment plans")

public class PlanController {


    private static final Logger logger = LogManager.getLogger(PlanController.class);
    @Autowired
    PlanGeneratorService generatorService;

    @PostMapping("/generate")
    @ApiOperation(value = "create an payment plan ", response = PaymentPlan.class)
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Server Exception")
    })
    public ResponseEntity createPlan(PaymentDTO dto) {
        PaymentInput input = null;
        try {
            input = new PaymentInput(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        PaymentPlan plan = generatorService.generatePlan(input);
        return ResponseEntity.ok(plan);
    }


}



