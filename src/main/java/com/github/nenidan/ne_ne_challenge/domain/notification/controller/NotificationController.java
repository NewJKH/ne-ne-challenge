package com.github.nenidan.ne_ne_challenge.domain.notification.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.nenidan.ne_ne_challenge.domain.notification.dto.request.NotificationReadRequest;
import com.github.nenidan.ne_ne_challenge.domain.notification.dto.request.NotificationRequest;
import com.github.nenidan.ne_ne_challenge.domain.notification.entity.NotificationType;
import com.github.nenidan.ne_ne_challenge.domain.notification.service.NotificationService;
import com.github.nenidan.ne_ne_challenge.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {
	private NotificationService notificationService;
	/*
		test
	 */
	@PostMapping
	public void testSend(){
		notificationService.send(new NotificationRequest("제목","내용", NotificationType.ADMIN_MESSAGE, 1L,null));
	}

	@PatchMapping("/notifications/{id}")
	public ResponseEntity<ApiResponse<Void>> read(@RequestBody NotificationReadRequest request,@PathVariable Long id){
		return ApiResponse.success(
			HttpStatus.OK,
			"알림 읽음 처리 완료",
			notificationService.read(request,id)
		);
	}
}