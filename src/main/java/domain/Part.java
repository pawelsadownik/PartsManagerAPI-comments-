package domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement
@Entity
@NamedQueries({
        @NamedQuery(name = "part.all" , query = "SELECT p FROM Part p"),
        @NamedQuery(name = "part.id", query = "FROM Part p WHERE p.id=:partId"),
        @NamedQuery(name = "part.price", query = "FROM Part p WHERE p.price BETWEEN :priceLow AND :priceHigh"),
        @NamedQuery(name = "part.name" , query = "FROM Part p WHERE p.name=:name"),
        @NamedQuery(name = "part.category", query = "FROM Part p WHERE p.category=:category")

})
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;
    private String category;


    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="part", orphanRemoval = true)
    private List<Comm> comments = new ArrayList<Comm>();

    @XmlTransient
    public List<Comm> getComments() {
        return comments;
    }

    public void setComments(List<Comm> comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
