package es.test.bank.entity;

public interface BaseEntity {
	/**
	 * Obtiene el ID del objeto.
	 * 
	 * @return ID del objeto
	 */
	String getId();

	/**
	 * Actualiza el ID del objeto.
	 * 
	 * @param id ID del objeto
	 */
	void setId(String id);

	/**
	 * Indica si el objeto ha sido persistido o es nuevo.
	 * 
	 * @return Verdadero si no ha sido persistido
	 */
	default boolean isNew() {
		return getId() == null;
	}

}
