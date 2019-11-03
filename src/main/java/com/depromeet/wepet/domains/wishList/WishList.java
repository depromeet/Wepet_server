package com.depromeet.wepet.domains.wishList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wishList")
@Entity
@Where(clause = "deleted = 0")
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_id")
    private long wishListId;

    @Column(name = "place_id")
    private String placeId;

    @Column(name = "device_id")
    private String deviceId;

    @JsonIgnore
    private boolean deleted;

    public void delete() {
        this.deleted = true;
    }

}
