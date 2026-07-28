package com.steverado.TeamWorkApi.mappers;

import com.steverado.TeamWorkApi.dtos.CommentDto;
import com.steverado.TeamWorkApi.entity.ArticleComment;
import com.steverado.TeamWorkApi.entity.GifComment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    ArticleComment toArticleCommentEntity(CommentDto commentDto);

    GifComment toGifCommentEntity(CommentDto commentDto);
}
