package com.abhishek.stories_app.service;

import com.abhishek.stories_app.dto.AdminStatsResponse;
import com.abhishek.stories_app.dto.UserResponse;
import com.abhishek.stories_app.mapper.UserMapper;
import com.abhishek.stories_app.repository.CommentRepository;
import com.abhishek.stories_app.repository.StoryRepository;
import com.abhishek.stories_app.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final UserRepository userRepository;
	private final StoryRepository storyRepository;
	private final CommentRepository commentRepository;
	private final StoryService storyService;
	private final CommentService commentService;

	@Transactional(readOnly = true)
	public List<UserResponse> listUsers() {
		return userRepository.findAll().stream().map(UserMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public AdminStatsResponse stats() {
		long users = userRepository.count();
		long posts = storyRepository.count();
		long comments = commentRepository.count();
		return new AdminStatsResponse(users, posts, comments);
	}

	@Transactional
	public void deleteStory(String id) {
		storyService.adminDeleteStory(id);
	}

	@Transactional
	public void deleteComment(Long id) {
		commentService.adminDelete(id);
	}
}
