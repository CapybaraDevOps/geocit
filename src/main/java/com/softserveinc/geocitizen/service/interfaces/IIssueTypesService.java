package com.softserveinc.geocitizen.service.interfaces;

import com.softserveinc.geocitizen.entity.Issue;

import java.util.List;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/30/18 at 1:04 PM
 */
public interface IIssueTypesService {

	List<Issue.Type> getAll();
}
