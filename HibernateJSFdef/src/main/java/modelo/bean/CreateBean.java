package modelo.bean;



import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;

import domain.Event;
import domain.Question;
import businessLogic.BLFacadeImplementation;

import org.primefaces.event.SelectEvent;

@ManagedBean(name = "createBean")
@ViewScoped
public class CreateBean {

	
	private List<Event> eventos;
	private BLFacadeImplementation blf;
	private Date date = null;
	private Event event;
	private String eventString;
	private String descripcion;
	private String minbet;

	public CreateBean() {

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMinbet() {
		return minbet;
	}

	public void setMinbet(String minbet) {
		this.minbet = minbet;
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
				int evInt = Integer.parseInt(eventString);
				if(evInt == i.getEventNumber()) {
					seleccionado = i;
				}
			}
			
			if(seleccionado == null) {
				System.out.println("No existe el evento");
			}else {
				System.out.println("El evento es: " + seleccionado);
				event = seleccionado;
			}
		}catch(Exception e) {
			System.out.println("No existe");
		}
	}
	
	public void createQuestion() {
		try {
			datosEventoSelec();
			if (event != null && descripcion != null && minbet != null) {
				if (descripcion.length() > 0) {					
					Question preg = blf.createQuestion(event, descripcion, Integer.valueOf(minbet));
					System.out.println("Pregunta creada: " + preg.getQuestion());
				} else
					System.out.println("Falla algun dato");
			} else
				System.out.println("Rellene todos los campos");
		} catch (NumberFormatException e) {
			System.out.println("Bet minimun debe ser un numero");
		} catch (Exception e) {
		
			System.out.println("Ya existe una apuesta con la misma descripción");
		}

	}

	public String volverInicio() {
		return "inicio";
	}

}