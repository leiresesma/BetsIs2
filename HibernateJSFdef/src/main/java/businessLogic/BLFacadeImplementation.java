package businessLogic;

import java.text.SimpleDateFormat;

import java.time.LocalDate;

import java.time.ZoneId;

import java.util.Date;
import java.util.List;

import java.util.Vector;

import org.hibernate.Session;

import dataAccess.HibernateDataAccess;
import domain.Question;
import domain.Event;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;

public class BLFacadeImplementation implements BLFacade {

	/**
	 * This method creates a question for an event, with a question text and the
	 * minimum bet
	 * 
	 * @param event      to which question is added
	 * @param question   text of the question
	 * @param betMinimum minimum quantity of the bet
	 * @return the created question, or null, or an exception
	 * @throws EventFinished        if current data is after data of the event
	 * @throws QuestionAlreadyExist if the same question already exists for the
	 *                              event
	 */

	public Question createQuestion(Event event, String question, float betMinimum)
			throws EventFinished, QuestionAlreadyExist {



		Session session = HibernateDataAccess.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		String q = question.replace("'", "''");
		List list = session.createQuery("from Question where question = '" + q + "'").list();
		if (list.size() > 0) {
		
			session.getTransaction().rollback();
			return null;
		} else {
			Question que = event.addQuestion(question, betMinimum);
			session.update(event);
			session.save(que);
			session.getTransaction().commit();
			return que;
		}
	};

	/**
	 * This method invokes the data access to retrieve the events of a given date
	 * 
	 * @param date in which events are retrieved
	 * @return collection of events
	 */

	public Vector<Event> getEvents(Date date) {


		Session session = HibernateDataAccess.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.format(date);
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		List l = session
				.createQuery("from Event where YEAR(eventDate) = " + localDate.getYear() + " and MONTH(eventDate) = "
						+ localDate.getMonthValue() + " and DAY(eventDate) = " + localDate.getDayOfMonth())
				.list();

		Vector<Event> eventos = new Vector<Event>();
		for (int i = 0; i < l.size(); i++) {
			Event ev = (Event) l.get(i);
			eventos.add(ev);
		}
		session.getTransaction().commit();

		return eventos;

	}

	/**
	 * This method invokes the data access to retrieve the dates a month for which
	 * there are events
	 * 
	 * @param date of the month for which days with events want to be retrieved
	 * @return collection of dates
	 */

	public Vector<Date> getEventsMonth(Date date) {



		Session session = HibernateDataAccess.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		simple.format(date);
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		List l = session.createQuery("from Event where YEAR(eventDate)  " + localDate.getYear()
				+ " and MONTH(eventDate) = " + localDate.getMonthValue()).list();

		Vector<Date> d = new Vector<Date>();
		for (int i = 0; i < l.size(); i++) {
			Date ev = (Date) l.get(i);
			d.add(ev);
		}
		session.getTransaction().commit();
		return d;

	}

}
