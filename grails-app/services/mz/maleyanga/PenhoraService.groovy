package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.pedidoDeCredito.Penhora

/**
 * PenhoraService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class PenhoraService {

    List<Penhora> getPenhoras(Cliente cliente) {
        return Penhora.findAllByCliente(cliente)
    }
}
