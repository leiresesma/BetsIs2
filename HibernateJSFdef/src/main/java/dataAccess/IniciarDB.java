package dataAccess;

import dataAccess.HibernateDataAccess;
import domain.Event;
import domain.Question;

import org.hibernate.Session;
import java.util.*;

public class IniciarDB {

	private List listaQuestions() {
		Session session = HibernateDataAccess.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List result = session.createQuery("from Question").list();
		session.getTransaction().commit();
		return result;
	}

	public static void main(String[] args) {
		IniciarDB e = new IniciarDB();
		System.out.println("Creación de eventos:");
		
		Session session = HibernateDataAccess.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Event e1 = new Event(1, "Pepe ha hecho login correctamente", new Date());
		List<Question> questions = new ArrayList<Question>();
		Question q1 = new Question(1, "Pregunta1", 1, e1);
		Question q2 = new Question(2, "Pregunta2", 2, e1);
		questions.add(q1);
		questions.add(q2);
		e1.setQuestions(questions);
		session.save(e1);
		session.save(q1);
		session.save(q2);
		
		Event e2 = new Event(2, "Nerea ha intentado hacer login", new Date());
		session.save(e2);
		
		Event e3 = new Event(3, "epa ha hecho login correctamente", new Date());
		session.save(e3);
		
		session.getTransaction().commit();
		
		
		System.out.println("Listado de eventos:");

		List quest= e.listaQuestions();
		for (int i = 0; i < quest.size(); i++) {
			Question ev = (Question) quest.get(i);
			System.out.println("Id: " + ev.getQuestionNumber() + "Query: " + ev.getQuestion() + " Fecha: " + ev.getBetMinimum());
		}
	}

}
