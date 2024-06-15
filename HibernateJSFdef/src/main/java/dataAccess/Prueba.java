package dataAccess;

import java.util.Date;

import java.util.Vector;

import businessLogic.BLFacadeImplementation;
import domain.Event;
import domain.Question;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;

public class Prueba {

	public static void main(String[] args) {
		BLFacadeImplementation blf = new BLFacadeImplementation();
		Vector<Event> eventos = blf.getEvents(new Date());
		
		for(int i =0; i < eventos.size(); i++) {
			Event ev = (Event) eventos.get(i);
			System.out.println(ev.getEventNumber());
		}
		
		try {
			Question q = blf.createQuestion(eventos.get(0), "Patata", 123);
		} catch (EventFinished e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (QuestionAlreadyExist q) {
			q.printStackTrace();
		}

	}

}
