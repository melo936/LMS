package ge.edu.ibsu.lms.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class Book {

    @Id
    private String id;

    public String title;
    public String author;
    public String isbn;

    // Using a User object exposes sensitive information
    //    public User borrower;
    public String borrowerId;

    @Version
    private int version;
}
