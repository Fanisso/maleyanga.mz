package mz.maleyanga.contrato

import mz.maleyanga.CreditoPedidoDeCreditoService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.entidade.Entidade
import mz.maleyanga.pedidoDeCredito.PedidoDeCredito
import org.zkoss.zul.Grid
import org.zkoss.zul.Label
import org.zkoss.zul.Row
import org.zkoss.zul.Rows


/**
 * Contrato
 * A domain class describes the data object and it's mapping to the database
 */
class Contrato {
    Grid grid
    String titulo
    Rows row_s
    Credito credito
    Cliente cliente
    Entidade entidade
    PedidoDeCredito pedidoDeCredito


    static hasMany = [blocos: Bloco]
    static mapping = {

    }

    static constraints = {
        blocos nullable: true
        pedidoDeCredito nullable: true
    }

    /*
     * Methods of the Domain Class
     */
//	@Override	// Override toString for a nicer / more descriptive UI 
    public String toString() {
        return "${titulo}"
    }
}
