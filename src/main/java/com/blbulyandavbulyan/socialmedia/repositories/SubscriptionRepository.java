package com.blbulyandavbulyan.socialmedia.repositories;

import com.blbulyandavbulyan.socialmedia.dtos.IFriend;
import com.blbulyandavbulyan.socialmedia.dtos.subcriptions.SubscriptionResponse;
import com.blbulyandavbulyan.socialmedia.entites.Subscription;
import com.blbulyandavbulyan.socialmedia.entites.keys.SubscriptionPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionPK> {

    @Transactional
    @Modifying
    @Query("""
            update Subscription s set s.viewed = :viewed
            where s.subscriberUsername = :subscriberUsername and s.targetUsername = :targetUsername""")
    int updateViewedBySubscriberAndTargetUsername(@Param("viewed") Boolean viewed, @Param("subscriberUsername") String subscriberUsername, @Param("targetUsername") String targetUsername);

    Page<SubscriptionResponse> findByTargetUsernameAndViewedIsFalse(String targetUsername, Pageable pageable);

    Page<SubscriptionResponse> findByTargetUsername(String targetUsername, Pageable pageable);

    @Query("""
            select s.subscriberUsername as friendUsername, GREATEST(s.creationDate, s2.creationDate) as friendshipStartDate from Subscription s
                inner join Subscription s2 on s2.subscriberUsername = s.targetUsername and s2.targetUsername = s.subscriberUsername
            where s.targetUsername = :target
            group by s.subscriberUsername, GREATEST(s.creationDate, s2.creationDate)
            """)
    Page<IFriend> findAllFriends(@Param("target") String targetUsername, Pageable pageable);
}