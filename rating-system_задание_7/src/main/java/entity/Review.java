package com.restaurant.rating.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(exclude = {"visitor", "restaurant"})
@ToString(exclude = {"visitor", "restaurant"})
public class Review {
    @EmbeddedId
    private ReviewId id;

    @MapsId("visitorId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @MapsId("restaurantId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private Integer rating;
    private String comment;

    public void setVisitor(Visitor v) {
        this.visitor = v;
        if (this.id == null) this.id = new ReviewId();
        this.id.setVisitorId(v.getId());
    }

    public void setRestaurant(Restaurant r) {
        this.restaurant = r;
        if (this.id == null) this.id = new ReviewId();
        this.id.setRestaurantId(r.getId());
    }
}