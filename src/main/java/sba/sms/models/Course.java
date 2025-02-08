package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Course is a POJO, configured as a persistent class that represents (or maps to) a table
 * name 'course' in the database. A Course object contains fields that represent course
 * information and a mapping of 'courses' that indicate an inverse or referencing side
 * of the relationship. Implement Lombok annotations to eliminate boilerplate code.
 */
@Entity
@Table(name = "course")
@Getter
@Setter
@AllArgsConstructor
@ToString(exclude = "students")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "instructor", length = 50, nullable = false)
    private String instructor;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Student> students = new HashSet<>();


    public Course() {
    }


    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
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

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }


    public void addStudent(Student student) {
        this.students.add(student);
        student.getCourses().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
