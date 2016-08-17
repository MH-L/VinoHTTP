import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "User")
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int userID;

    @Column(name = "name", length = 20, unique = true)
    private String username;

    @Column(name = "administrator")
    private boolean administrator;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "registrationDate", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private Date registrationDate;

    @Column(name = "city")
    private String locationCity;

    @Lob
    @Column(name = "profilePicture")
    private String profilePicPath;

    @OneToMany(mappedBy = "user")
    private Collection<Comment> postedComments;

    @ManyToMany(mappedBy = "likedUsers")
    private Collection<Article> likedArticles;

    @ManyToMany
    @JoinTable(name = "User_Star_Wines",
            joinColumns = {@JoinColumn(name = "userID")},
            inverseJoinColumns = {@JoinColumn(name = "wineID")})
    private Collection<Wine> staredWines;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public Collection<Comment> getPostedComments() {
        return postedComments;
    }

    public void setPostedComments(Collection<Comment> postedComments) {
        this.postedComments = postedComments;
    }

    public Collection<Wine> getStaredWines() {
        return staredWines;
    }

    public void setStaredWines(Collection<Wine> staredWines) {
        this.staredWines = staredWines;
    }

    public Collection<Article> getLikedArticles() {
        return likedArticles;
    }

    public void setLikedArticles(Collection<Article> likedArticles) {
        this.likedArticles = likedArticles;
    }
}
