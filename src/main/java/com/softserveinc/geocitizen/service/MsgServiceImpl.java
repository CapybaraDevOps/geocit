package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.entity.Msg;
import com.softserveinc.geocitizen.repository.MsgRepository;
import com.softserveinc.geocitizen.service.interfaces.IMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Repository
public class MsgServiceImpl implements IMsgService {

	private final MsgRepository msgRepository;

	@Autowired
	public MsgServiceImpl(MsgRepository msgRepository) {
		this.msgRepository = msgRepository;
	}

	@Override
	public Msg findById(String id) {
		return msgRepository.findById(id);
	}

	@Override
	public List<Msg> findAll() {
		return msgRepository.findAll();
	}

	@Override
	public Msg save(String id, String text) {
		return msgRepository.save(new Msg(id, text));
	}
}
