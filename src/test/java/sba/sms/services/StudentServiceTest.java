package sba.sms.services;

import org.junit.jupiter.api.Test;
import sba.sms.models.Student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


class StudentServiceTest {

    StudentService studentService = new StudentService();

    @Test
    void testCreateStudent(){
        Student student = new Student("test@email.com", "Test Name", "password123");
        studentService.createStudent(student);
        Student retrieved = studentService.getStudentByEmail(student.getEmail());
        assertNotNull(retrieved);
        assertEquals(student.getEmail(), retrieved.getEmail());
    }


}


//Do test//