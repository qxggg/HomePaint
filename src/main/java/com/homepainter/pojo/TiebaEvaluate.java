package com.homepainter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TiebaEvaluate {
    int tiebaId;
    String detail;
    int evaluateId;
    String username;
    List<EvaluateImage> evaluateImage;
    int userId;

    public TiebaEvaluate(String detail, int tiebaId, String username, List<EvaluateImage> evaluateImage, int userId) {
        this.detail = detail;
        this.tiebaId = tiebaId;
        this.username = username;
        this.evaluateImage = evaluateImage;
        this.userId = userId;
    }
}
