package modelo.bean;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacadeImplementation;
import domain.Event;
import domain.Question;

@ManagedBean(name = "queryBean")
@ViewScoped
public class QueryBean {

    private List<Event> eventos;
    private BLFacadeImplementation blf;
    private Date date = null;
    private Event event;
    private String eventString;
    private List<Question> questions;

    public QueryBean() {
        blf = Inicio.getBLF();
        eventos = blf.getEvents(new Date());
        eventString = "";
    }

    public String getEventString() {
        return eventString;
    }

    public void setEventString(String eventString) {
        this.eventString = eventString;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Event> getEventos() {
        return eventos;
    }

    public void setEventos(List<Event> eventos) {
        this.eventos = eventos;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void getEventosDay() {
        if (date != null) {
            eventos = blf.getEvents(date);
            if (eventos.size() > 0) {
            	
                System.out.println("Hay eventos");
            } else {
                System.out.println("No hay eventos");
            }
        } else {
            System.out.println("No date selected");
        }
    }

    public void onDateSelect(SelectEvent event) {
        this.date = (Date) event.getObject();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fecha escogida: " + date));
        getEventosDay();
    }

    public void onEventSelect() {
        System.out.println("Evento seleccionado: " + eventString);
    }

    public void datosEventoSelec() {
        Event seleccionado = null;
        try {
            for (Event i : eventos) {
                int evInt = Integer.valueOf(eventString);
                if (evInt == i.getEventNumber()) {
                    seleccionado = i;
                }
            }

            if (seleccionado == null) {
                System.out.println("No existe el evento");
            } else {
                System.out.println("El evento es: " + seleccionado);
                event = seleccionado;
                System.out.println("Hola " );
            }
        } catch (Exception e) {
            System.out.println("No existe");
        }
    }

    public void queryQuestion() {
        datosEventoSelec();
        if (event != null) {
            questions = event.getQuestions();
            for (Question q : questions) {
                System.out.println("Question ID: " + q.getQuestionNumber() + ", Description: " + q.getQuestion());
            }
        } else {
            questions = new ArrayList<Question>();
            System.out.println("No event selected.");
        }
    }

    public String volverInicio() {
        return "inicio";
    }
}
