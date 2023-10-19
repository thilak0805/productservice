package com.appsdeveloperblog.estore.productservice.command;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/management")
public class EventsReplayController {

    @Autowired
    private EventProcessingConfiguration eventProcessingConfiguration;

    @PostMapping("/eventprocessor/{processorname}/reset")
    public ResponseEntity<String> replayEvents(@PathVariable String processorname){
        Optional<TrackingEventProcessor> trackingEventProcessor = eventProcessingConfiguration.eventProcessor(processorname, TrackingEventProcessor.class);
        if(trackingEventProcessor.isPresent()){
            TrackingEventProcessor eventProcessor = trackingEventProcessor.get();
            //shut it down
            eventProcessor.shutDown();
            //reset to initial state
            eventProcessor.resetTokens();
            //now tokens are reset we can start replaying events
            eventProcessor.start();
            return ResponseEntity.ok().body(String.format("The event processor with name [%s] has been reset ", processorname));
        }else{
            return ResponseEntity.badRequest()
                    .body(String.format("The event processor with a name [%s] is not a tracking event processor", processorname));
        }
    }
}
