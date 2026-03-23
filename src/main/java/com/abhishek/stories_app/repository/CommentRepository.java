package com.abhishek.stories_app.repository;

import com.abhishek.stories_app.model.Comment;
import com.abhishek.stories_app.model.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@EntityGraph(attributePaths = "user")
	Page<Comment> findByStoryOrderByCreatedAtAsc(Story story, Pageable pageable);

	long countByStory(Story story);
}
