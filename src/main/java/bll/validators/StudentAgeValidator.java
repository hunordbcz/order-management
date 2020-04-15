package bll.validators;

import model.Client;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @Since: Apr 03, 2017
 */
public class StudentAgeValidator implements Validator<Client> {
    private static final int MIN_AGE = 7;
    private static final int MAX_AGE = 30;

    @Override
    public void validate(Client client) {

    }

//	public void validate(Student t) {
//
//		if (t.getAge() < MIN_AGE || t.getAge() > MAX_AGE) {
//			throw new IllegalArgumentException("The Student Age limit is not respected!");
//		}
//
//	}

}
