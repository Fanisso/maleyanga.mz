package mz.maleyanga.pagamento

import mz.maleyanga.conta.Conta
import mz.maleyanga.diario.Diario
import mz.maleyanga.documento.Anexo
import mz.maleyanga.security.Utilizador

/**
 * A  Parcela de pagam
 * A domain class describes the data object and it's mapping to the database
 */
class Remissao implements Serializable {
    private static final long serialVersionUID = 1
     String descricao
    BigDecimal valorDaRemissao = 0.0
    BigDecimal percentual = 0.0
    Date createdDate
    Utilizador utilizador
    Conta contaOrigem
    Diario diario
   // static hasMany = [anexos: Anexo]
    static belongsTo = [pagamento: Pagamento]

    static mapping = {

    }

    static constraints = {
        utilizador nullable: false
        valorDaRemissao nullable: false, scale: 2
        pagamento nullable: false
        descricao nullable: true
        percentual nullable: true, min: 0.0, max: 100.0, scale: 2
        diario nullable: true
        contaOrigem nullable: true
        //  anexos nullable: true
    }

    /*
     * Methods of the Domain Class
     */
//	@Override	// Override toString for a nicer / more descriptive UI 
    String toString() {
        return "${descricao + "-" + valorDaRemissao}"
    }
}
