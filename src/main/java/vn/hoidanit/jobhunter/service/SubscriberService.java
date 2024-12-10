package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.response.subscriber.ResCreateSubscriberDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;

    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    // Create
    public Subscriber createSubscriber(Subscriber subscriber) {
        return subscriberRepository.save(subscriber);
    }

    public ResCreateSubscriberDTO convertResCreateSubscriberDTO(Subscriber subscriber) {
        ResCreateSubscriberDTO resCreateSubscriberDTO = new ResCreateSubscriberDTO();
        List<ResCreateSubscriberDTO.SkillSubscriber> rSkillSubscriber = new ArrayList<ResCreateSubscriberDTO.SkillSubscriber>();
        List<Skill> skillList = subscriber.getSkills();
        skillList.forEach(item -> {
            ResCreateSubscriberDTO.SkillSubscriber skillSubscriber = new ResCreateSubscriberDTO.SkillSubscriber();
            Skill findSkill = this.skillRepository.findById(item.getId()).get();
            skillSubscriber.setName(findSkill.getName());
            rSkillSubscriber.add(skillSubscriber);
        });
        resCreateSubscriberDTO.setName(subscriber.getName());
        resCreateSubscriberDTO.setEmail(subscriber.getEmail());
        resCreateSubscriberDTO.setSkills(rSkillSubscriber);
        return resCreateSubscriberDTO;
    }
}
