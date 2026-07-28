package com.steverado.TeamWorkApi.mappers;

import com.steverado.TeamWorkApi.dtos.FeedItemDto;
import com.steverado.TeamWorkApi.entity.Article;
import com.steverado.TeamWorkApi.entity.Gif;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedMapper {

    @Mapping(source = "createdAt", target = "createdOn")
    @Mapping(source = "user.id", target = "authorId")
    FeedItemDto feedArticle(Article article);

    @Mapping(source = "imageUrl", target = "content")
    @Mapping(source = "createdAt", target = "createdOn")
    @Mapping(source = "user.id", target = "authorId")
    FeedItemDto feedGif(Gif gif);
}
