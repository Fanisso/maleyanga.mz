package mz.maleyanga.Taxa

import mz.maleyanga.entidade.Entidade
import mz.maleyanga.security.Utilizador

/**
 * Taxa
 * A domain class describes the data object and it's mapping to the database
 */
class Taxa implements Serializable{
    private static final long serialVersionUID = 1
    String nome
	BigDecimal valor =0
    BigDecimal percentagem
    boolean activo = true
    Entidade entidade
    BigDecimal valorMinimo
    BigDecimal valorMaximo
    String recorencia
 Utilizador utilizador


	
//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
//	static	hasMany		= []	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping 
	
    static	mapping = {
        id generator: 'increment'
    }
    
	static	constraints = {
        recorencia nullable: true
        utilizador nullable:  true
       nome  nullable:false
        valor nullable: true
        percentagem nullable: true, min: 0.0, max: 100.0, scale: 2
        valorMinimo nullable: true
        valorMaximo nullable: true

    }

    /*
     * Methods of the Domain Class
     */

    @Override
    // Override toString for a nicer / more descriptive UI
    String toString() {
        return "${nome}"
    }
}
