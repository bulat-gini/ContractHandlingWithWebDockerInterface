package ru.itis.dockerweb.managmentservice.controllers;

import com.spotify.docker.client.DockerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.itis.dockerweb.managmentservice.models.Replica;
import ru.itis.dockerweb.managmentservice.services.interfaces.ReplicaService;

import java.util.List;

/**
 * @author Bulat Giniyatullin
 * 11 November 2018
 */

@RestController
@RequestMapping("/api/replica")
public class ReplicaController {
    @Autowired
    private ReplicaService replicaService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public String createReplica() throws DockerException, InterruptedException {
        return replicaService.createReplica();
    }

    @DeleteMapping
    public void deleteReplica(@RequestBody String id) throws DockerException, InterruptedException, IllegalArgumentException {
        replicaService.removeReplica(id);
    }

    @GetMapping
    public List<Replica> getAll() {
        return replicaService.getAll();
    }

    @Scheduled(fixedDelay = 5000)
    public void sendReplicasUpdate() throws InterruptedException {
        simpMessagingTemplate.convertAndSend(
                "/topic/replicasListUpdate",
                replicaService.pullReplicasUpdate());
    }
}
