package com.example.springOAuth.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springOAuth.entity.EventType;
import com.example.springOAuth.entity.User;
import com.example.springOAuth.model.EventTypeRequest;
import com.example.springOAuth.repository.EventTypeRepository;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.response.EventTypeResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventTypeService {

        private final UserRepository userRepository;
        private final EventTypeRepository eventTypeRepository;
        @Autowired
        private ModelMapper modelMapper;

        public EventTypeResponse createEventTypeHandler(EventTypeRequest entity,
                        User currentUser) {
                User user = userRepository.findByEmail(currentUser.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                var enventType = EventType.builder().title(entity.getTitle()).description(entity.getDescription())
                                .duration(entity.getDuration()).user(user).build();
                var savedEvent = eventTypeRepository.save(enventType);

                return modelMapper.map(savedEvent, EventTypeResponse.class);

        }

        public List<EventTypeResponse> getUserEventType(User currentUser) {
                User user = userRepository.findByEmail(currentUser.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                var enventType = eventTypeRepository.findByUser(user);
                var mappedEvent = enventType.stream().map(item -> modelMapper.map(item, EventTypeResponse.class))
                                .toList();
                return mappedEvent;
        }

        public EventTypeResponse updateEventHandler(Long id, EventTypeRequest entity, User currentUser) {
                EventType eventType = eventTypeRepository.findById(id)
                                .orElseThrow(() -> new UsernameNotFoundException("Event not found"));

                eventType.setDescription(entity.getDescription());
                eventType.setDuration(entity.getDuration());
                eventType.setTitle(entity.getTitle());
                var savedEvent = eventTypeRepository.save(eventType);
                return modelMapper.map(savedEvent, EventTypeResponse.class);
        }

        public void deleteEventHandler(Long id, User currentUser) {
                EventType eventType = eventTypeRepository.findById(id)
                                .orElseThrow(() -> new UsernameNotFoundException("Event not found"));

                if (eventType.getUser().getId() != currentUser.getId()) {
                        throw new UsernameNotFoundException("Unauthorized action");
                }

                eventTypeRepository.delete(eventType);
        }

}
