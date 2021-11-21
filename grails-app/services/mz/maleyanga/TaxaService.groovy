package mz.maleyanga

import mz.maleyanga.Taxa.Taxa
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.settings.DefinicaoDeCredito
import org.springframework.transaction.annotation.Transactional

/**
 * TaxaService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class TaxaService {

    def calcularTaxas(Cliente cliente, DefinicaoDeCredito dc) {
        def creditos = Credito.findAllByClienteAndPeriodicidade(cliente, dc.periodicidade)
        def recorencias_de_taxas = dc.taxa.recorencia.split()
        System.println(recorencias_de_taxas)

    }
}
