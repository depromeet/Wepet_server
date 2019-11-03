package com.depromeet.wepet.domains.category;

import com.depromeet.wepet.domains.common.supports.ModifiedAuditor;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "category")
@Entity
@Where(clause = "deleted = 0")
public class Category extends ModifiedAuditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long categoryId;

    @Column
    private String displayName;

    @Column
    private String searchKeyword;

    @Column
    @JsonIgnore
    private boolean deleted = false;

    public void delete() {
        this.deleted = true;
    }

    public static Category getWishListCategory() {
        return Category
                .builder()
                .categoryId(new Long(-1))
                .displayName("즐겨찾기")
                .searchKeyword("wishList")
                .build();
    }
}
