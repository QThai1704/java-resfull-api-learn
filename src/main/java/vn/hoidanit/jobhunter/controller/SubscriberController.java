package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.response.subscriber.ResCreateSubscriberDTO;
import vn.hoidanit.jobhunter.domain.response.subscriber.ResUpdateSubscriberDTO;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<ResCreateSubscriberDTO> createSubscriber(@RequestBody Subscriber subscriber) throws IdInvalidException {
        boolean isExistEmail = this.subscriberService.existsByEmailInSub(subscriber.getEmail());
        if (isExistEmail) {
                throw new IdInvalidException("Email " + subscriber.getEmail() + " đã tồn tại.");
        }
        Subscriber newSubscriber = subscriberService.createSubscriber(subscriber);
        ResCreateSubscriberDTO resCreateSubscriberDTO = this.subscriberService.convertResCreateSubscriberDTO(newSubscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateSubscriberDTO);
    }

    // Update
    @PutMapping("/subscribers")
    public ResponseEntity<ResUpdateSubscriberDTO> updateSubscriber(@RequestBody Subscriber subscriber) {
        Subscriber currentSubscriber = this.subscriberService.updateSubscriber(subscriber);
        ResUpdateSubscriberDTO resUpdateSubscriberDTO = this.subscriberService.convertResUpdateSubscriberDTO(currentSubscriber);
        return ResponseEntity.ok().body(resUpdateSubscriberDTO);
    }

    // Delete
    @DeleteMapping("/subscribers/{id}")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable long id) {
        this.subscriberService.deleteSubscriber(id);
        return ResponseEntity.ok().body(null);
    }
}
