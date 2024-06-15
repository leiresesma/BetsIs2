package businessLogic;

import java.text.SimpleDateFormat;


import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
//hola
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;


import org.hibernate.Session;


import dataAccess.HibernateDataAccess;
import domain.Question;
import domain.Event;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;


public class BLFacadeImplementation implements BLFacade {
//	DataAccessInterface dbManager;

	public BLFacadeImplementation() {
//		System.out.println("Creating BLFacadeImplementation instance");
//		ConfigXML c = ConfigXML.getInstance();

		/*
		 * if (c.getDataBaseOpenMode().equals("initialize")) {
		 * 
		 * dbManager=new DataAccessInterface(new ObjectDbDAOManager());
		 * dbManager.initializeDB(); dbManager.close(); }
		 */
	}

//	public BLFacadeImplementation(DataAccessInterface da) {
//
////		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
////		ConfigXML c = ConfigXML.getInstance();
////
////		if (c.getDataBaseOpenMode().equals("initialize")) {
////			da.emptyDatabase();
////			da.open();
////			da.initializeDB();
////			da.close();
////
////		}
////		dbManager = da;
//	}

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

		System.out.println("Crear pregunta");

		Session session = HibernateDataAccess.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		String q = question.replace("'", "''");
		List list = session.createQuery("from Question where question = '" + q + "'").list();
		if (list.size() > 0) {
			System.out.println("La pregunta ya existe para este evento");
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
		System.out.println("Coger Eventos");

		Session session = HibernateDataAccess.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = sdf.format(date);
		System.out.println("Dia deprecated: " + date.getYear() + date.getMonth() + date.getDay());
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		System.out.println("Dia: " + localDate.getYear() + localDate.getMonthValue() + localDate.getDayOfMonth());
		List l = session
				.createQuery("from Event where YEAR(eventDate) = " + localDate.getYear() + " and MONTH(eventDate) = "
						+ localDate.getMonthValue() + " and DAY(eventDate) = " + localDate.getDayOfMonth())
				.list();
		System.out.println("Dias: " + l.size());
		Vector<Event> eventos = new Vector<Event>();
		for (int i = 0; i < l.size(); i++) {
			Event ev = (Event) l.get(i);
			eventos.add(ev);
		}
		session.getTransaction().commit();
		;
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

		System.out.println("Coger Eventos");

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
		;
		return d;

	}

//	public void close() {
//		// DataAccess dB4oManager=new DataAccess(false);
//
//		// dB4oManager.close();
//		dbManager.close();
//
//	}

	/**
	 * This method invokes the data access to initialize the database with some
	 * events and questions. It is invoked only when the option "initialize" is
	 * declared in the tag dataBaseOpenMode of resources/config.xml file
	 */
//	@WebMethod
//	public void initializeBD() {
//		dbManager.open();
//		dbManager.initializeDB();
//		dbManager.close();
//	}

}
