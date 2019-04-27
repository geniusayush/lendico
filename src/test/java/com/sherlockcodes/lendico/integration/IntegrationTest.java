package com.sherlockcodes.lendico.integration;

import com.sherlockcodes.lendico.models.PaymentDTO;
import com.sherlockcodes.lendico.models.PaymentInfo;
import com.sherlockcodes.lendico.models.PaymentPlan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;

    @Test
    public void testCreateStudent() throws Exception {
        HttpEntity<PaymentDTO> entity = new HttpEntity<PaymentDTO>(new PaymentDTO("5000", "5", 1, "2018-01-01T00:00:01Z"), headers);
        ResponseEntity<PaymentPlan> response = restTemplate.exchange(
                createURLWithPort("/plan/generate"), HttpMethod.POST, entity, PaymentPlan.class);
        PaymentInfo plan = response.getBody().getPlans().get(0);

        assertEquals(plan.getBorrowerPaymentAmount(), 5020.83, 0.5);
        assertEquals(plan.getDate().toString(), "2018-01-01");
        assertEquals(plan.getInitialOutstandingPrincipal(), 5000, 0.5);
        assertEquals(plan.getInterest(), 20.83, 0.5);
        assertEquals(plan.getPrincipal(), 5000, 0.5);
        assertEquals(plan.getRemainingOutstandingPrincipal(), 0, 0.5);


    }

    @Test
    public void testInvaldNumbers() throws Exception {
        HttpEntity<PaymentDTO> entity = new HttpEntity<PaymentDTO>(new PaymentDTO("no_NUMBER", "5", 1, "2018-01-01T00:00:01Z"), headers);
        ResponseEntity<PaymentPlan> response = restTemplate.exchange(
                createURLWithPort("/plan/generate"), HttpMethod.POST, entity, PaymentPlan.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testInvaldDates() throws Exception {
        HttpEntity<PaymentDTO> entity = new HttpEntity<PaymentDTO>(new PaymentDTO("5000", "5", 1, "2018-01-GBG01Z"), headers);
        ResponseEntity<PaymentPlan> response = restTemplate.exchange(
                createURLWithPort("/plan/generate"), HttpMethod.POST, entity, PaymentPlan.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testNegetiveNumbers() throws Exception {
        HttpEntity<PaymentDTO> entity = new HttpEntity<PaymentDTO>(new PaymentDTO("-50", "5", -1, "2018-01-GBG01Z"), headers);
        ResponseEntity<PaymentPlan> response = restTemplate.exchange(
                createURLWithPort("/plan/generate"), HttpMethod.POST, entity, PaymentPlan.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}