package com.chamika.blog.mappers;

import com.chamika.blog.domain.CreatePostRequest;
import com.chamika.blog.domain.UpdatePostRequest;
import com.chamika.blog.domain.dtos.CreatePostRequestDto;
import com.chamika.blog.domain.dtos.PostDto;
import com.chamika.blog.domain.dtos.UpdatePostRequestDto;
import com.chamika.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

   
    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);

    @Mapping(target = "status", source = "status")
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);
}
