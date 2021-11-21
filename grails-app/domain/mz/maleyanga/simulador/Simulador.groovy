package mz.maleyanga.simulador

/**
 * Simulador
 * A domain class describes the data object and it's mapping to the database
 */
class Simulador implements Serializable {
    private static final long serialVersionUID = 1
    BigDecimal rate  // Required. The interest rate for the loan.
    int nper   // Required. The total number of payments for the loan.
    BigDecimal pv //Required. The present value, or the total amount that a series of future payments is worth now; also known as the principal.
    BigDecimal fv // Optional. The future value, or a cash balance you want to attain after the last payment is made. If fv is omitted, it is assumed to be 0 (zero), that is, the future value of a loan is 0.
    int typ // Optional. The number 0 (zero) or 1 and indicates when payments are due.
	/* Default (injected) attributes of GORM */


//	Long	id
//	Long	version
	
	/* Automatic timestamping of GORM */
//	Date	dateCreated
//	Date	lastUpdated
	
//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
	static	hasMany		= [items:Item]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping 
	
/*    static	mapping = {

    }*/
    static mapWith = 'none'
    
	static	constraints = {
    }
	
	/*
	 * Methods of the Domain Class
	 */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}
}
