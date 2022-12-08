package com.example.springawsdocker.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books_db")
public class Book implements Serializable {

   @Serial
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, length = 180)
   private String author;

   @Column(name = "launch_date", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date launchDate;

   @Column(nullable = false)
   private Double price;

   @Column(nullable = false, length = 250)
   private String title;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Book book = (Book) o;
      return id.equals(book.id) && author.equals(book.author) && launchDate.equals(book.launchDate) && price.equals(book.price) && title.equals(book.title);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, author, launchDate, price, title);
   }
}
