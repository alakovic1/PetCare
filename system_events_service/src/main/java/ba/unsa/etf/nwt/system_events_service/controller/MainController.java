package ba.unsa.etf.nwt.system_events_service.controller;

import ba.unsa.etf.nwt.system_events_service.model.Action;
import ba.unsa.etf.nwt.system_events_service.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private ActionRepository actionRepository;

    @GetMapping("/actions")
    public List<Action> getUsers() {
        return actionRepository.findAll();
    }

}
