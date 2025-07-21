package com.github.nenidan.ne_ne_challenge.domain.notification.service;

import org.springframework.stereotype.Service;

import com.github.nenidan.ne_ne_challenge.domain.notification.dto.request.NotificationReadRequest;
import com.github.nenidan.ne_ne_challenge.domain.notification.dto.request.NotificationRequest;

@Service
public interface NotificationService {
	void send(NotificationRequest notificationRequest);

	Void read(NotificationReadRequest request, Long id);
}
