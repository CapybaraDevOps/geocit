package com.softserveinc.geocitizen.service.interfaces;

import com.softserveinc.geocitizen.dto.MapDataDTO;
import com.softserveinc.geocitizen.entity.Issue;
import com.softserveinc.geocitizen.exception.AbstractCitizenException;
import com.softserveinc.geocitizen.exception.BadFieldFormatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IIssueService {

//	Issue saveIssue(MapDataDTO dto, MultipartFile image) throws BadFieldFormatException;

	Issue getById(Integer id);

	List<Issue> getAllIssueByMapMarker(int mapMarkerId);

	Issue findById(Integer id) throws AbstractCitizenException;

	Page<Issue> findByTitleOrText(String title, String text, String author, Pageable pageable);

	Page<Issue> findAuthorId(Integer id, Pageable pageable);

	Page<Issue> findAll(Pageable pageable);

	Integer deleteById(Integer id) throws AbstractCitizenException;

	Integer toggleClosed(Integer id) throws AbstractCitizenException;

	Integer toggleHidden(Integer id) throws AbstractCitizenException;

	Page<Issue> findClosedTrue(Pageable pageable);

	Page<Issue> findClosedFalse(Pageable pageable);

	Page<Issue> findByHiddenTrue(Pageable pageable);

	Page<Issue> findByHiddenFalse(Pageable pageable);

	Page<Issue> findByType(String type, Pageable pageable);

	Page<Issue> findByClosedAndHiddenAndType(boolean closed, boolean hidden, String type, Pageable pageable);

	Issue saveIssue(MapDataDTO dto) throws BadFieldFormatException;

	Issue setImage(Integer id, MultipartFile image) throws BadFieldFormatException;
}
