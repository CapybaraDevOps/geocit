package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.entity.Issue;
import com.softserveinc.geocitizen.repository.IssueTypesRepository;
import com.softserveinc.geocitizen.service.interfaces.IIssueTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/30/18 at 1:05 PM
 */
@Service
@Transactional
public class IssueTypesService implements IIssueTypesService {

	private final IssueTypesRepository repository;

	@Autowired
	public IssueTypesService(final IssueTypesRepository repository) {
		this.repository = repository;
	}

	@Override
	@ReadOnlyProperty
	public List<Issue.Type> getAll() {
		return repository.findAll();
	}
}
