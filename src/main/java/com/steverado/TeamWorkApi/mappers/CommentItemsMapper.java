package com.steverado.TeamWorkApi.mappers;

import com.steverado.TeamWorkApi.dtos.CommentItemsDto;
import com.steverado.TeamWorkApi.entity.ArticleComment;
import com.steverado.TeamWorkApi.entity.GifComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentItemsMapper {

    @Mapping(source = "comment_id", target = "commentId")
    @Mapping(source = "user.id", target = "authorId")
    CommentItemsDto gifComment(GifComment comment);

    @Mapping(source = "comment_id", target = "commentId")
    @Mapping(source = "user.id", target = "authorId")
    CommentItemsDto articleComment(ArticleComment comment);
}
