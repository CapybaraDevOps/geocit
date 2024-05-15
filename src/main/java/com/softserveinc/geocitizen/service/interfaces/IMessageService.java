package com.softserveinc.geocitizen.service.interfaces;

import com.softserveinc.geocitizen.dto.ChatRoom;
import com.softserveinc.geocitizen.entity.FullMessage;
import com.softserveinc.geocitizen.exception.AccessDeniedException;

import java.util.List;

public interface IMessageService {

	FullMessage saveMessage(FullMessage message);

	List<FullMessage> getAllMessages() throws AccessDeniedException;

	FullMessage getMessage(Long id) throws AccessDeniedException;

	boolean checkChat(Long issueId, Long userId) throws AccessDeniedException;

	List<FullMessage> getAllMessagesForChat(Long issueId, Long userId) throws AccessDeniedException;

	List<ChatRoom> getAllChatRooms(Long adminId);

	boolean checkAccessForAdmin(Long issueId, Long userId, Long adminId) throws AccessDeniedException;
}
