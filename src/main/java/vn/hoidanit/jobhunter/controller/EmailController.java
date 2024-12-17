package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/emails")
    @ApiMessage("Send simple email")
    // @Scheduled(cron = "*/30 * * * * *")
    public String sendSimpleEmail() {
        // this.emailService.sendEmailSync("quangthai170402@gmail.com", "Test email", "<h1><b>Test email</b></h1>", false, true);
        this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }
}
