package com.restaurant.rating.entity;

import javax.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor
public class ReviewId implements Serializable {
    private Long visitorId;
    private Long restaurantId;
}