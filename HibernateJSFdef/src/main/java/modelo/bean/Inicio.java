package modelo.bean;

import businessLogic.BLFacadeImplementation;

public class Inicio {

	private static BLFacadeImplementation blf = null;

	public static BLFacadeImplementation getBLF() {
		return blf;
	}

	public Inicio() {
		if (blf == null)
			blf = new BLFacadeImplementation();
	}

	public String queryQuestions() {
		return "query";
	}

	public String createQuestions() {
		return "create";
	}

}
