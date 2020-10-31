package net.skideo.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String videoLink;
    @ManyToMany(cascade = CascadeType.PERSIST,targetEntity = Like.class,fetch = FetchType.LAZY)
    private Set<Like> likes;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Video() {}

    public Video(String videoLink) {
        this.videoLink=videoLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
