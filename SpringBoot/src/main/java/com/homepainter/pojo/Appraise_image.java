package com.homepainter.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Appraise_image {

    int appraiseId;
    String imageUrl;

    String imageId;

    public Appraise_image(int appraiseId, String imageUrl) {
        this.appraiseId = appraiseId;
        this.imageUrl = imageUrl;
    }
}
