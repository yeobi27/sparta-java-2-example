package com.yeobi.mall.domain.user.repository;

import static com.yeobi.mall.domain.purchase.entity.QPurchase.purchase;
import static com.yeobi.mall.domain.user.entity.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeobi.mall.domain.user.dto.QUserPurchaseResponse;
import com.yeobi.mall.domain.user.dto.UserPurchaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

// @Repository 로 스프링에 사용등록
@Repository
@RequiredArgsConstructor
public class UserQueryRepository {  // UserQueryRepository 이름은 선택적 Query 를 구분자로 사용한것일뿐

  // QueryDSL 사용가능
  private final JPAQueryFactory queryFactory;

  // Pagable , Page : 페이지네이션
  public Page<UserPurchaseResponse> findUsers(String name, String email, Pageable pageable) {
    List<UserPurchaseResponse> users = queryFactory
        // .select(user)// 메인 컨텐츠가 되는 값
        // 사실상 user 만 찾는 경우는 기본적인 경우
        // UserPurchaseResponse 를 사용한 이유는 내가 원하는 Query 로 사용하기 위해서.
        .select(new QUserPurchaseResponse(
            user.id,
            user.name,
            user.email,
            purchase.id,
            purchase.totalPrice.as("price")
        ))// 메인 컨텐츠가 되는 값
        .from(user)
        .join(purchase)
        .on(user.eq(purchase.user))  // 구매품목의 유저 == 유저, 변수말고도 객체비교로도 가져올수있다.
        .where(
            // user.name.eq(name).or(user.email.eq(email))
            // user.name.eq(name), (user.email.eq(email))
            // like 와 같이 언저리로 문자열로 검색하겠다! 하면 contains
            // ',' 쉼표표시는 and 와 같다.
            nameContains(name),
            emailContains(email)
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    //offset:
    //→ "앞에서부터 몇 개를 건너뛰고 가져올지"
    //예) offset 0이면 처음부터, offset 10이면 11번째부터
    //
    //limit:
    //→ "한 번에 몇 개를 가져올지" (페이지 사이즈)
    //예) limit 10이면 10개씩

    Long totalCount = queryFactory.select(user.count())  // 메인 컨텐츠가 되는 값
        .from(user)
        .join(purchase)
        .on(user.eq(purchase.user))  // 구매품목의 유저 == 유저, 변수말고도 객체비교로도 가져올수있다.
        .where(
            // user.name.eq(name).or(user.email.eq(email))
            // user.name.eq(name), (user.email.eq(email))
            // like 와 같이 언저리로 문자열로 검색하겠다! 하면 contains
            // ',' 쉼표표시는 and 와 같다.
            nameContains(name),
            emailContains(email)
        )
        .fetchOne();

    return new PageImpl<>(users, pageable, totalCount);
  }

  // 메소드 나누기
  private BooleanExpression nameContains(String name) {
    return StringUtils.hasText(name) ? user.name.contains(name) : null;
  }

  private BooleanExpression emailContains(String email) {
    return StringUtils.hasText(email) ? user.name.contains(email) : null;
  }
}


