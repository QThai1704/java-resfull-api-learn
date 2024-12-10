package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.response.subscriber.ResCreateSubscriberDTO;
import vn.hoidanit.jobhunter.service.SubscriberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    // Create
    @PostMapping("/subscribers")
    public ResponseEntity<ResCreateSubscriberDTO> createSubscriber(@RequestBody Subscriber subscriber) {
        Subscriber newSubscriber = subscriberService.createSubscriber(subscriber);
        ResCreateSubscriberDTO resCreateSubscriberDTO = this.subscriberService.convertResCreateSubscriberDTO(newSubscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateSubscriberDTO);
    }

    // Update
    // Delete
}
