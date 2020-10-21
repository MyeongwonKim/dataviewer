package com.sam.dataviewer.domain;

import lombok.Getter;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashBoard;

    private String title;

    private String content;

    private String file;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Estimate> estimates = new ArrayList<>();

    /*
    * 생성 메서드
      order 생성시 회원 id가 있어야 한다. 이를 위해서는 session을 사용하여 로그인된 회원의 id를 갖고 있어야 한다.
      그리고 dashboard의 경우 연관관계의 주인을 dashboard로 지정하는 것이 좋을 듯 하다. 왜냐하면 order 생성시
      꼭 dashboard id가 있을 필요가 없기 때문이다. order 생성 후 결제가 이루어지면 그 때 dashboard가 생성되면 되기 때문임
     */
//    public static Order createOrder()
}
