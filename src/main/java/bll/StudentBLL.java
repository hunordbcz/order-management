package bll;

import bll.validators.EmailValidator;
import bll.validators.StudentAgeValidator;
import bll.validators.Validator;
import dao.StudentDAO;
import model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @Since: Apr 03, 2017
 */
public class StudentBLL {

	private StudentDAO studentDAO;
	protected List<Validator<Student>> validators;

	public StudentBLL() {
		validators = new ArrayList<Validator<Student>>();
		validators.add(new EmailValidator());
		validators.add(new StudentAgeValidator());

		studentDAO = new StudentDAO();
	}

	public Student findById(int id) {

		Student st = studentDAO.findById(id);
		if (st == null) {
			throw new NoSuchElementException("The student with id =" + id + " was not found!");
		}
		return st;
	}

	public Boolean insert(Student student) {
		return studentDAO.insert(student);
	}

	public Boolean update(Student student) {
		return studentDAO.update(student);
	}

	public List<Student> findAll() {
		return studentDAO.findAll();
	}
}
