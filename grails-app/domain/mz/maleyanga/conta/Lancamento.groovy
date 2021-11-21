package mz.maleyanga.conta

import mz.maleyanga.security.Utilizador

/**
 * Lancamento
 * A domain class describes the data object and it's mapping to the database
 */
class Lancamento {
    BigDecimal valor
    String historico
    String formula
    boolean credito
    Utilizador utilizador
    Conta credora
    Conta devedora
    /*
    LANÇAMENTO DE PRIMEIRA FÓRMULA
 É o registro do fato contábil que envolve uma conta devedora e outra conta credora.
    * LANÇAMENTO DE SEGUNDA FÓRMULA
 o registro do fato contábil que envolve uma conta devedora e mais de uma conta credora.
LANÇAMENTO DE TERCEIRA FÓRMULA
É o registro do fato contábil envolvendo mais de uma conta devedora e apenas uma conta credora.
LANÇAMENTO DE QUARTA FÓRMULA
É o registro do fato contábil que envolve mais de uma conta devedora e mais de uma conta credora

    *   */
    /* Default (injected) attributes of GORM */
//	Long	id
//	Long	version

    /* Automatic timestamping of GORM */
    Date dateCreated
    Date lastUpdated

//	static	belongsTo	= [Conta]	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
//	static	hasMany		= [credoras:Conta,devedoras:Conta]	// tells GORM to associate other domain objects for a 1-n or n-m mappinstatic	mappedBy	= []	// specifies which property should be used in a mapping

    static mapping = {
    }

    static constraints = {
        formula inList: ["primeira", "segunda", "terceira", "quarta"]
    }

    /*
     * Methods of the Domain Class
     */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}
}
