package se.accelerateit.signup6.integrationtest.dao;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.accelerateit.signup6.dao.MembershipMapper;
import se.accelerateit.signup6.integrationtest.SignupDbTest;
import se.accelerateit.signup6.model.Membership;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = {SignupDbTest.Initializer.class})
public class MembershipMapperTest extends SignupDbTest{

    static final Logger logger = LoggerFactory.getLogger(MembershipMapperTest.class);

    private final MembershipMapper membershipMapper;

    @Autowired
    MembershipMapperTest(MembershipMapper membershipMapper) {
        this.membershipMapper = membershipMapper;
    }

    @Test
    void findMembershipForEvent() {

        assertEquals(-1L, membershipMapper.findMembershipForEvent(-1L, -2L).orElseThrow(), "User -1 should be member of group where event -2 is planned");
        assertEquals(-2L, membershipMapper.findMembershipForEvent(-2L, -2L).orElseThrow(), "User -2 should be member of group where event -2 is planned");
        assertEquals(-3L, membershipMapper.findMembershipForEvent(-3L, -3L).orElseThrow(), "User -3 should be member of group where event -3 is planned");

        assertFalse(membershipMapper.findMembershipForEvent(-5L, -2L).isPresent(), "User -5 should NOT be member of group where event -2 is planned");
        assertFalse(membershipMapper.findMembershipForEvent(-1L, -222L).isPresent(), "User -1 should NOT be member of group for a non-existent event -222");
        assertFalse(membershipMapper.findMembershipForEvent(-111L, -2L).isPresent(), "Non-existing user -111 NOT be member of group where event -2 is planned");
        assertFalse(membershipMapper.findMembershipForEvent(-111L, -222L).isPresent(), "Non-existing user -111 NOT be member of group for a non-existent event -222");

    }

    @Test
    void findByUserAndGroup(){
        Optional<Membership> dbResponse = membershipMapper.findByUserAndGroup(-1L, -1L);
        assertTrue(dbResponse.isPresent());
        logger.info("Existing user and group = {}", dbResponse);
        Membership membership = dbResponse.get();

        assertEquals(-1L,membership.getId());
        assertEquals(-1L, membership.getUserId());
        assertEquals(-1L, membership.getGroupId());

        Optional<Membership> nonExistingGroup = membershipMapper.findByUserAndGroup(-1L, -5000000L);
        assertFalse(nonExistingGroup.isPresent());
        logger.info("Non existing group = {}", nonExistingGroup);

        Optional<Membership> nonExistingUser = membershipMapper.findByUserAndGroup(-1000000000L, -1L);
        assertFalse(nonExistingUser.isPresent());
        logger.info("Non existing user = {}", nonExistingUser);

        Optional<Membership> nonExistingUserAndGroup = membershipMapper.findByUserAndGroup(-1000000000L, -1000000000L);
        assertFalse(nonExistingUserAndGroup.isPresent());
        logger.info("Non existing user and group = {}", nonExistingUserAndGroup);
    }

    @Test
    void findUsersByGroup() {
        List<Membership> dbResponse = membershipMapper.findUsersByGroup(-2L);
        assertFalse(dbResponse.isEmpty());

        Membership membershipOne = dbResponse.get(0);
        logger.info("Membership one = {}", membershipOne);
        assertEquals(-3L, membershipOne.getId());
        assertEquals(-3L, membershipOne.getUserId());
        assertEquals(-2L, membershipOne.getGroupId());

        Membership membershipTwo = dbResponse.get(1);
        logger.info("Membership two = {}", membershipTwo);
        assertEquals(-4L, membershipTwo.getId());
        assertEquals(-4L, membershipTwo.getUserId());
        assertEquals(-2L, membershipTwo.getGroupId());

        List<Membership> nonExistingGroup = membershipMapper.findUsersByGroup(-2222222L);
        assertTrue(nonExistingGroup.isEmpty());
    }

}
