package mz.maleyanga.transacao

import mz.maleyanga.conta.Conta

/**
 * Transacao
 * A domain class describes the data object and it's mapping to the database
 */
class Transacao implements Serializable {
    private static final long serialVersionUID = 1
    BigDecimal valor = 0.0
    String descricao
    boolean credito
    BigDecimal credit
    BigDecimal debito
    BigDecimal saldo
    /* Default (injected) attributes of GORM */
//	Long	id
//	Long	version

    /* Automatic timestamping of GORM */
    Date dateCreated
//	Date	lastUpdated

//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
//	static	hasMany		= []	// tells GORM to associate other domain objects for a 1-n or n-m mapping
//	static	mappedBy	= []	// specifies which property should be used in a mapping
	
    static	mapping = {

    }

    static constraints = {
        descricao nullable: true
        dateCreated nullable: true
        credit nullable: true
        debito nullable: true
        saldo nullable: true
    }

    /*
     * Methods of the Domain Class
     */

    BigDecimal getCredit() {
        if (credito) {
            return valor
        }
        return 0.0
    }

    BigDecimal getDebito() {
        if (!credito) {
            return valor
        }
        return 0.0
    }

    @Override
    // Override toString for a nicer / more descriptive UI
    String toString() {

        return "${descricao + '. Credito:' + credito + '. Valor:' + valor}"
    }
}
