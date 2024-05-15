package com.softserveinc.geocitizen.repository;

import com.softserveinc.geocitizen.entity.Msg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsgRepository extends MongoRepository<Msg, String> {

	Msg findById(String id);

	List<Msg> findAll();

	Msg save(Msg msg);
}
