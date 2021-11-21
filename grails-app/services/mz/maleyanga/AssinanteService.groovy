package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.cliente.Assinante
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.security.Utilizador

/**
 * AssinanteService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class AssinanteService {

    boolean saveAssinante(Assinante assinante) {
        assinante.utilizador = Utilizador.findById(assinante.cliente.utilizador.id)
        System.println(assinante.utilizador)
        try {
            assinante.save flush: true
            System.println(assinante.id)
            Assinante assinanteDB = Assinante.findById(assinante.id)
            if (assinanteDB) {
                return true
            } else {
                System.println("erro nao gravacao do assinante!")
                return false
            }


        } catch (Exception e) {
            System.println(e.toString())
            return false
        }


    }

    boolean mergeAssinante(Assinante assinante) {
        try {
            assinante.merge(flush: true)
            return true
        } catch (Exception e) {
            System.println(e.toString())
            return false
        }
    }

    boolean deleteAssinante(Assinante assinante) {
        try {
            assinante.delete()
            return true
        } catch (Exception e) {
            System.println(e.toString())
            return false
        }
    }
}
