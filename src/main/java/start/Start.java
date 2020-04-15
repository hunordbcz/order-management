package start;

import bll.StudentBLL;
import model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 *          Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @Since: Apr 03, 2017
 */
public class Start {
	protected static final Logger LOGGER = Logger.getLogger(Start.class.getName());

	public static void main(String[] args) throws SQLException {

		StudentBLL studentBll = new StudentBLL();

		Student student1 = null;
		List<Student> studentList = null;
		try {
			student1 = studentBll.findById(1245);
//			student1.setId(1);
//			studentBll.insert(student1);
			student1.setId(20);
			studentBll.update(student1);
		} catch (Exception ex) {
			LOGGER.log(Level.INFO, ex.getMessage());
		}

		// obtain field-value pairs for object through reflection
		ReflectionExample.retrieveProperties(student1);

	}

}
