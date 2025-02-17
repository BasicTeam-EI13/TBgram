package com.tbgram.domain.friends.entity;

import com.tbgram.domain.common.entity.BaseEntity;
import com.tbgram.domain.friends.enums.RequestStatus;
import com.tbgram.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name ="Friends")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Friends extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public Friends(Member sender, Member receiver, RequestStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public void updateStatus(RequestStatus status){
        this.status = status;
    }
}
