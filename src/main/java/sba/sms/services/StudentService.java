package sba.sms.services;

import lombok.extern.java.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI {

    @Override
    public void createStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> getAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean validateStudent(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student WHERE email = :email AND password = :password", Student.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            return query.uniqueResult() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, email);
            Course course = session.get(Course.class, courseId);

            if (student != null && course != null) {
                if (!student.getCourses().contains(course)) {
                    student.addCourse(course);
                    session.merge(student);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNativeQuery("SELECT * FROM course c INNER JOIN student_courses sc ON c.id = sc.courses_id WHERE sc.student_email = :email", Course.class)
                    .setParameter("email", email)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}