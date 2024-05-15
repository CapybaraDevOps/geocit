package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.dto.ChatRoom;
import com.softserveinc.geocitizen.entity.FullMessage;
import com.softserveinc.geocitizen.entity.Message;
import com.softserveinc.geocitizen.entity.Msg;
import com.softserveinc.geocitizen.entity.User;
import com.softserveinc.geocitizen.exception.AccessDeniedException;
import com.softserveinc.geocitizen.repository.IssuesRepository;
import com.softserveinc.geocitizen.repository.MessageRepository;
import com.softserveinc.geocitizen.repository.MsgRepository;
import com.softserveinc.geocitizen.repository.UsersRepository;
import com.softserveinc.geocitizen.security.model.AuthorizedUser;
import com.softserveinc.geocitizen.service.interfaces.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements IMessageService {

	private final MessageRepository messageRepository;
	private final IssuesRepository issuesRepository;
	private final UsersRepository usersRepository;
	private final MsgRepository msgRepository;

	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository,
	                          IssuesRepository issuesRepository,
	                          UsersRepository usersRepository,
	                          MsgRepository msgRepository) {
		this.messageRepository = messageRepository;
		this.issuesRepository = issuesRepository;
		this.usersRepository = usersRepository;
		this.msgRepository = msgRepository;
	}

//	@Override
//	public FullMessage saveMessage(FullMessage message) {
//		return messageRepository.save(message);
//	}

	@Override
	public FullMessage saveMessage(FullMessage message) {
//		FullMessage msg = messageRepository.getOne(message.getId());
//		if (msg.getMsgid() == null) {
//			return messageRepository.save(message);
//		}
		if (messageRepository.existsByIssueIdAndUserId(message.getIssueId(), message.getUserId())) {
			msgRepository.save(new Msg(message.getIssueId().toString(), message.getText()));
			return messageRepository.save(message);
		}
		return messageRepository.save(message);
	}


	@Override
	public List<FullMessage> getAllMessages() throws AccessDeniedException {
		if (AuthorizedUser.getCurrent().getType().equals(User.Type.ROLE_ADMIN)) {
			return messageRepository.findAll();
		}
		throw new AccessDeniedException();
	}

	@Override
	public FullMessage getMessage(Long id) throws AccessDeniedException {
		if (AuthorizedUser.getCurrent().getType().equals(User.Type.ROLE_ADMIN)) {
			return messageRepository.findOne(id);
		}
		throw new AccessDeniedException();
	}

	@Override
	public boolean checkChat(Long issueId, Long userId) throws AccessDeniedException {
		if (new Long(AuthorizedUser.getCurrent().getId()).equals(userId) ||
				AuthorizedUser.getCurrent().getType().equals(User.Type.ROLE_ADMIN)) {
			return messageRepository.existsByIssueIdAndUserId(issueId, userId);
		}
		throw new AccessDeniedException();
	}

	@Override
	public List<FullMessage> getAllMessagesForChat(Long issueId, Long userId) throws AccessDeniedException {
		if (new Long(AuthorizedUser.getCurrent().getId()).equals(userId) ||
				AuthorizedUser.getCurrent().getType().equals(User.Type.ROLE_ADMIN)) {
			return messageRepository.findAllByIssueIdAndUserId(issueId, userId);
		}
		throw new AccessDeniedException();
	}

	@Override
	public List<ChatRoom> getAllChatRooms(Long adminId) {
		List<FullMessage> chatRoomMessages = messageRepository.findAllChatRooms(adminId);
		List<ChatRoom> chatRooms = new ArrayList<>();
		for (FullMessage chatRoom : chatRoomMessages) {
			int userId = Integer.valueOf(chatRoom.getUserId().toString());
			int issueId = Integer.valueOf(chatRoom.getIssueId().toString());
			String login = usersRepository.findById(userId).get().getLogin();
			String issueTitle = issuesRepository.findById(issueId).get().getTitle();
			chatRooms.add(new ChatRoom(login, issueTitle, userId, issueId));
		}
		return chatRooms;
	}

	@Override
	public boolean checkAccessForAdmin(Long issueId, Long userId, Long adminId) throws AccessDeniedException {
		if (new Long(AuthorizedUser.getCurrent().getId()).equals(adminId) ||
				AuthorizedUser.getCurrent().getType().equals(User.Type.ROLE_ADMIN)) {
			return messageRepository.existsByIssueIdAndUserIdAndAuthorId(issueId, userId, adminId);
		}
		throw new AccessDeniedException();
	}
}
