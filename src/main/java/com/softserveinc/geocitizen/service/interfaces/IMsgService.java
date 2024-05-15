package com.softserveinc.geocitizen.service.interfaces;

import com.softserveinc.geocitizen.entity.Msg;

import java.util.List;

public interface IMsgService {

	Msg findById(String id);

	List<Msg> findAll();

	Msg save(String id, String text);
}
