package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    private Integer age; // 나이 추가
    private Integer gender; //성별 추가

    private String city;
    private String street;
    private String zipcode;
}
