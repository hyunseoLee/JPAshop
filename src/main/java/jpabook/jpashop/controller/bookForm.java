package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class bookForm {

    @NotEmpty(message = "이름은 필수값입니다.")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

}
