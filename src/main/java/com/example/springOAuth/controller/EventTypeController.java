package com.example.springOAuth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springOAuth.entity.User;
import com.example.springOAuth.model.EventTypeRequest;
import com.example.springOAuth.response.EventTypeResponse;
import com.example.springOAuth.service.EventTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/event-type")
public class EventTypeController {

        @Autowired
        private EventTypeService eventTypeService;

        @Operation(security = { @SecurityRequirement(name = "bearer-key") })
        @ApiResponse(responseCode = "201", description = "Successful operation", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = EventTypeResponse.class))
        })
        @PostMapping("")
        public ResponseEntity<?> createEventType(@Valid @RequestBody EventTypeRequest entity,
                        @AuthenticationPrincipal User currentUser) {

                var savedEnventType = eventTypeService.createEventTypeHandler(entity, currentUser);

                return ResponseEntity.status(HttpStatus.OK).body(savedEnventType);
        }

        @Operation(security = { @SecurityRequirement(name = "bearer-key") })
        @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventTypeResponse.class))))
        @GetMapping("")
        public ResponseEntity<List<EventTypeResponse>> getEventType(
                        @AuthenticationPrincipal User currentUser) {

                var eventTypes = eventTypeService.getUserEventType(currentUser);
                return ResponseEntity.status(HttpStatus.OK).body(eventTypes);
        }

        @ApiResponse(responseCode = "201", description = "Successful operation", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = EventTypeResponse.class))
        })
        @GetMapping("/{userId}/{slug}")
        public ResponseEntity<EventTypeResponse> getEventTypeBySlug(@PathVariable("userId") String userId,
                        @PathVariable("slug") String slug) {

                var eventTypes = eventTypeService.findUserEventTypeBySlug(slug, userId);
                return ResponseEntity.status(HttpStatus.OK).body(eventTypes);
        }

        @Operation(security = { @SecurityRequirement(name = "bearer-key") })
        @PutMapping("/{id}")
        public ResponseEntity<?> updateEventType(@PathVariable("id") Long id,
                        @Valid @RequestBody EventTypeRequest entity,
                        @AuthenticationPrincipal User currentUser) {

                var eventTypes = eventTypeService.updateEventHandler(id, entity, currentUser);
                return ResponseEntity.status(HttpStatus.OK).body(eventTypes);
        }

        @Operation(security = { @SecurityRequirement(name = "bearer-key") })
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteEventType(@PathVariable("id") Long id,
                        @AuthenticationPrincipal User currentUser) {

                eventTypeService.deleteEventHandler(id, currentUser);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Success");
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

}
