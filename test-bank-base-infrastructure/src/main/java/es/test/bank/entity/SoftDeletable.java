package es.test.bank.entity;

public interface SoftDeletable {

	/**
	 * Obtiene el estado respecto a la baja logica.
	 * 
	 * @return Estado actual
	 */
	boolean isDeleted();
	
}
