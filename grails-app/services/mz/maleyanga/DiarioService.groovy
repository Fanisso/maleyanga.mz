package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.diario.Diario

/**
 * DiarioService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class DiarioService {

    Diario diario

    Diario findByEstado(String estado) {
        return Diario.findByEstado(estado)
    }

    def save(Diario diario) {
        diario.save(flush: true)
    }

    def getDiario() {
        diario = Diario.findByEstado("aberto")

        if (diario == null) {
            diario = Diario.findByEstado("pendente")
        }
        if (diario?.estado == "fechado") {
            return null
        }
        return diario
    }
}
