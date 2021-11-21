package mz.maleyanga


import mz.maleyanga.pedidoDeCredito.PedidoDeCredito

import java.sql.SQLDataException

/**
 * PedidoDeCreditoService
 * A service class encapsulates the core business logic of a Grails application
 */

class PedidoDeCreditoService {

    def validarPedido(PedidoDeCredito pedidoDeCreditoInstance) {
        if (pedidoDeCreditoInstance.valorDeCredito < pedidoDeCreditoInstance.valorDaPenhora * 2) {

            respond pedidoDeCreditoInstance.errors, view: 'edit'
            return

        }
    }

    def save(PedidoDeCredito pedidoDeCredito) {
        try {
            pedidoDeCredito.save()
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }

    def merge(PedidoDeCredito pedidoDeCredito) {
        try {
            pedidoDeCredito.merge(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }
}
