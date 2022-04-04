package mz.maleyanga.transferencia

import mz.maleyanga.conta.Conta
import mz.maleyanga.diario.Diario
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.security.Utilizador

/**
 * Transferencia
 * A domain class describes the data object and it's mapping to the database
 */
class Transferencia {
    Conta origem
    Conta destino
    String descricao
    BigDecimal valor
    Utilizador utilizador
    String contaOrigem
    String contaDestino
    Parcela parcela
    Diario diario
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

    static mapping = {
        origem lazy: false
        destino lazy: false
        batchSize(5)
    }

    static constraints = {
        contaDestino nullable: true
        contaOrigem nullable: true
        parcela nullable: true
        diario nullable: true

    }

    String getContaOrigem() {
        return origem.designacaoDaConta + ""
    }

    String getContaDestino() {
        return destino.designacaoDaConta + ""
    }
/*
	 * Methods of the Domain Class
	 */
//	@Override	// Override toString for a nicer / more descriptive UI 
    String toString() {
        return "${descricao}"
    }
}
