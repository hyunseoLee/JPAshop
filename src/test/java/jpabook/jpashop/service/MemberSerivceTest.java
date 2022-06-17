package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional  // rollback 가능하도록
public class MemberSerivceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;



    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setName("kim");
        //When
        Long saveId = memberService.join(member);
        //Then
        em.flush();
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception
    {
        //given
        Member member1= new Member();
        member1.setName( "kim");

        Member member2= new Member();
        member2.setName( "kim");

        //when
        memberService.join(member1);
        /*
        try{
            memberService.join(member2); // 예외발생해야함
        }catch(IllegalStateException e){
            return;
        }
         */
        memberService.join(member2); // 예외발생해야함

        //then
        fail("예외가 발생해야하는데, 예외 발생이 안됐나보군요.");

    }

    @Test
    @Rollback(false)
    public void 회원_수정(){

        // 1. 저장
        Member member= new Member();
        member.setName("원형");
        member.setAge(20);
        member.setGender(1);
        member.setAddress(new Address("서울","111","11111"));

        System.out.println("작업 전 -> 변경전 이름 값 찾아내기 : " + member.getName());
        memberService.join(member);

        Member findMember = memberService.findOne(member.getId());
        findMember.setName("김원형");
        memberService.updateItem(findMember.getId(), findMember.getName(),
                                    findMember.getAge(), findMember.getGender(),
                                    findMember.getAddress().getCity(),
                                    findMember.getAddress().getStreet(),
                                    findMember.getAddress().getZipcode());

        //Assert.assertEquals(book, itemService.findOne(book.getId()));
        Member getMember = memberService.findOne(findMember.getId());

        System.out.println("작업 후 -> 변경전 이름 값 찾아내기 : " + member.getName());
        System.out.println("변경한 이름 값 찾아내기 : " + getMember.getName());
        Assert.assertEquals("김원형", getMember.getName());


    }

}