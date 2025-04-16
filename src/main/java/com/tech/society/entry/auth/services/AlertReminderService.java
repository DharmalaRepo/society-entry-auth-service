package com.tech.society.entry.auth.services;

import com.tech.society.residents.models.AlertReminder;
import com.tech.society.residents.repositories.AlertReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertReminderService {

    @Autowired
    private AlertReminderRepository repository;

    public AlertReminder createReminder(AlertReminder reminder) {
        return repository.save(reminder);
    }

    public List<AlertReminder> getUpcomingReminders() {
        return repository.findByTriggerDateBefore(LocalDateTime.now().plusDays(7));
    }
}