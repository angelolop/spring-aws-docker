package com.example.springawsdocker.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"id", "author", "launchDate","price", "title"})
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

   @Serial
   private static final long serialVersionUID = 1L;

   @Mapping("id")
   @JsonProperty("id")
   private Long key;

   private String author;

   private Date launchDate;

   private Double price;

   private String title;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;
      BookVO bookVO = (BookVO) o;
      return key.equals(bookVO.key) && author.equals(bookVO.author) && launchDate.equals(bookVO.launchDate) && price.equals(bookVO.price) && title.equals(bookVO.title);
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), key, author, launchDate, price, title);
   }
}
