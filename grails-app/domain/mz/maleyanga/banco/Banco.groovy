package mz.maleyanga.banco


/**
 * Banco
 * A domain class describes the data object and it's mapping to the database
 */
class Banco {
    String nomeDoBanco
    String agencia
    String combinacao
	/* Default (injected) attributes of GORM */
//	Long	id
//	Long	version
	
	/* Automatic timestamping of GORM */
//	Date	dateCreated
//	Date	lastUpdated

//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
//	static	hasMany		= []	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping 

    static mapping = {
    }

    static constraints = {
        combinacao unique: true
    }

    /*
     * Methods of the Domain Class
     */
//	@Override	// Override toString for a nicer / more descriptive UI 
    String toString() {
        return "${nomeDoBanco}"
    }

    String getCombinacao() {
        return nomeDoBanco + "-" + agencia
    }
}
