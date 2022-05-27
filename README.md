# JPAshop
[inflean] 스프링부트와 JPA활용- 웹 어플리케이션 개발 

# 목차
## 1. 프로젝트 환경설정 
  * 프로젝트 생성
  * 라이브러리 살펴보기 
  * View 환경 설정
  * H2 데이터베이스 설치 
  * JPA와 DB 설정, 동작확인
## 2. 도메인 분석 설계 
  * 요구사항 분석
  * 도메인 모델과 테이블 설계 
  * 엔티티 클래스 개발 
  * 엔티티 설계시 주의점
## 3. 애플리케이션 구현 준비 
  * 구현 요구사항
  * 애플리케이션 아키텍처 
## 4. 회원 도메인 개발
  * 회원 리포지토리 개발 
  * 회원 서비스 개발
  * 회원 기능 테스트
## 5. 상품 도메인 개발
  * 상품 엔티티 개발(비즈니스 로직 추가) 
  * 상품 리포지토리 개발
  * 상품 서비스 개발
## 6. 주문 도메인 개발
  * 주문, 주문상품 엔티티 개발 주문 리포지토리 개발
  * 주문 서비스 개발
  * 주문 기능 테스트
  * 주문 검색 기능 개발
     
## 7. 웹 계층 개발
  * 홈 화면과 레이아웃
  * 회원 등록
  * 회원 목록 조회
  * 상품 등록
  * 상품 목록
  * 상품 수정
  * 변경 감지와 병합(merge) 상품 주문
  * 주문 목록 검색, 취소


## 환경설정
https://start.spring.io 
* Project : Gradle 
* Language : Java 
* Spring Boot : 2.6.4
* Dependencies 
 * Spring Web Starter 
 * Thymeleaf 
 * H2 Database 
 * Lombok 
 * Spring Data JPA 


## 요구사항 분석
<img width="446" alt="스크린샷 2022-05-26 오후 6 39 14" src="https://user-images.githubusercontent.com/33533199/170462137-680f89d5-dcd2-4379-a8e2-2f963d9d42f4.png">

* 회원 기능
  * 회원 가입
  * 회원 조회
* 상품 기능 
  * 상품 등록
  * 상품 수정
  * 상품 조회 
* 주문  
  * 상품 주문
  * 주문 내역 조회
  * 주문 취소
* 기타 요구사항
  * 상품은 재고 관리가 필요하다.
  * 상품의 종류는 도서, 음반, 영화가 있다. 상품을 카테고리로 구분할 수 있다.
## 도메인모델
<img width="485" alt="스크린샷 2022-05-26 오후 6 39 45" src="https://user-images.githubusercontent.com/33533199/170462218-665edfec-8052-4d30-83ec-a97d16dffdef.png">

## 엔티티 분석

<img width="488" alt="스크린샷 2022-05-26 오후 6 40 54" src="https://user-images.githubusercontent.com/33533199/170462411-de5b3b50-b4b8-4224-adad-ffd5590557fb.png">

> 연관관계의 매핑 분석 : **외래키가 있는 주문 을 연관관계의 주인으로 하는 것이 좋다.**
* 회원과 주문 : 일대다, 다대일의 양방향 관계
 * 그러므로 Order.member 를 ORDERS.MEMBER_ID 외래키와 매핑한다. 
* 주문상품과 주문 : 다대일 양방향 관계다. 
 * 외래 키가 주문상품에 있으므로 주문상품이 연관관계의 주인이다.
 * 그러므로 OrderItem.order를 ORDER_ITEM.ORDER_ID 외래키와 매핑한다.
* 주문상품과 상품: 다대일 양방향 관계다. 
  * OrderItem.item 을 ORDERITEM.ITEM_ID 외래 키와 매핑한다.
* 주문과 배송 : 일대일 양방향 관계다.
 *  Order.delivery를 ORDERS.DELIVER_ID 외래키와 매핑한다.
* 카테고리와 상품 : @ManyToMany 를 사용해서 매핑한다. (실무에서는 @ManyToMany는 사용하지 말자)

## 테이블 분석 
<img width="482" alt="스크린샷 2022-05-26 오후 6 40 38" src="https://user-images.githubusercontent.com/33533199/170462366-929062fe-53d3-4c80-a2c6-83ae0122f18c.png">

## 애플리케이션 구현 준비  
* 개발순서:
1. 도메인 -> 서비스, 리포지토리를 개발 하고
2. 테이스 케이스 작성해서 검증
3. 마지막에 컨트롤러 통해서 웹 계층 적용 
 
도메인-> 서비스,리포지토리 -> 테스트케이스 -> 컨트롤러 -> 웹 계층 


------------------------------------------------------------------------------------------------------------------------------------

## 회원 도메인 개발
* 회원 도메인 : Member
* 회원 Repository: MemberRepository
 * 저장 : save 
 * 회원 (단건/다건)조회 : findOne, findAll
* 회원 Service : MemberService 
 * 생성자 주입
* 회원 기능 test

  * @Runwith(SpringRunner.class): 스프링과 테스트 통합
  * @SpringBootTest : 스프링 부트 띄우고 테스트(이게 없으면 @Autowired 다 실패)
  *  @Transactional : 반복 가능한 테스트 지원, 각각의 테스트를 실행할 때마다 트랜잭션을 시작하고 테스트가 끝나면 트랜잭션을 강제로 롤백 (이 어노테이션이 테스트 케이스에서 사용될 때만 롤백)
  *  @Transactional 에노테이션 사용하면 데이터베이스에 데이터가 저장안되고 rollback이 되기때문에, 직접 확인해보고 싶다면 @Rollback(false) 를 사용하면 된다. 

```java
try{
            memberService.join(member2); // 예외발생해야함
        }catch(IllegalStateException e){
            return;
        }
```
* try-catch 문 대신 에노테이션에 발생될 exception을 적어줘서 사용할 수 있다. 
```java
@Test(expected = IllegalStateException.class)
```

 * test할때  WAS 위에 메모리DB 사용하는 법
   * test>resource>application.yml 의 spring 관련 내용을 모두 주석처리하면 스프링부트에서 자동으로 메모리db를 실행시킨다.


