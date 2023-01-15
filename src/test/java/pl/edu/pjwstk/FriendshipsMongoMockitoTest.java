package pl.edu.pjwstk;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FriendshipsMongoMockitoTest {
    @Mock
    FriendsCollection friends;

    @InjectMocks
    FriendshipsMongo friendships;

    @Test
    public void alexDoesNotHaveFriends() {
        when(friends.findByName("Alex")).thenReturn(null);
        assertThat(friendships.getFriendsList("Alex")).isEmpty();
    }

    @Test
    public void joeHas5Friends() {
        Person joe = new Person("Joe");
        joe.addFriend("Karol");
        joe.addFriend("Dawid");
        joe.addFriend("Maciej");
        joe.addFriend("Tomek");
        joe.addFriend("Adam");

        when(friends.findByName("Joe")).thenReturn(joe);
        List<String> expected = Arrays.asList("Karol", "Dawid", "Maciej", "Tomek", "Adam");
        assertThat(friendships.getFriendsList("Joe")).hasSize(5).containsOnly("Karol", "Dawid", "Maciej", "Tomek", "Adam");
    }

    @Test
    public void testMakeFriends() {
        Person p1 = new Person("person1");
        Person p2 = new Person("person2");
        when(friends.findByName("person1")).thenReturn(p1);
        when(friends.findByName("person2")).thenReturn(p2);
        friendships.makeFriends("person1", "person2");
        assertThat(p1.getFriends()).contains("person2");
        assertThat(p2.getFriends()).contains("person1");
    }

    @Test
    public void testAreFriendsWhenNotFriends() {
        Person p1 = new Person("person1");
        when(friends.findByName("person1")).thenReturn(p1);
        assertFalse(friendships.areFriends("person1", "person2"));
    }

    @Test
    public void testAreFriendsWhenOneIsNotInDB() {
        when(friends.findByName("person1")).thenReturn(null);
        assertFalse(friendships.areFriends("person1", "person2"));
    }
}