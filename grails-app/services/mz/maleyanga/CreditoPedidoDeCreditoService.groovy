package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.credito.Credito



/**
 * CreditoPedidoDeCreditoService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class CreditoPedidoDeCreditoService {

    def serviceMethod() {

    }


    def actualizarPedidoDeCredito(Credito creditoInstance){
         Credito.findAllByPedidoDeCredito(creditoInstance.pedidoDeCredito.setCredito(creditoInstance.save()))

    }
}
