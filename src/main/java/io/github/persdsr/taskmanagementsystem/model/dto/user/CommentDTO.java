package io.github.persdsr.taskmanagementsystem.model.dto.user;

import io.github.persdsr.taskmanagementsystem.entity.CommentEntity;
import lombok.Data;

@Data
public class CommentDTO {
    private String text;
    private String author;

    public static CommentDTO toModel(CommentEntity comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText(comment.getText());
        commentDTO.setAuthor(comment.getAuthor().getUsername());

        return commentDTO;
    }
}
