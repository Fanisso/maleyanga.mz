package mz.maleyanga.credito


import grails.transaction.Transactional
import mz.maleyanga.BasicController
import mz.maleyanga.CurrencyWriter
import mz.maleyanga.entidade.Entidade
import mz.maleyanga.settings.Settings
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

/**
 * CreditoController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = false)
@ActionLogging
@SpringUserIdentification
class CreditoController extends BasicController {
    def sessionStorageService

    static allowedMethods = [save: ["POST", "PUT"], update: ["PUT", "POST"], delete: "DELETE"]

    @Secured(['ROLE_ADMIN', 'CREDITO_INDEX'])
    def credito() {
    }

    @Secured(['ROLE_ADMIN', 'CREDITO_INDEX'])
    def showSimulador() {
    }

    @Secured(['ROLE_ADMIN', 'CREDITO_INDEX'])
    def createCredito() {
    }

    def printExtratoDeCredito() {
        Credito credito = sessionStorageService.credito as Credito
        [nome: credito?.cliente?.nome]
    }


    def printContratoDeCredito() {
        Credito credito = sessionStorageService.getCredito() as Credito
        [nome: credito.cliente.nome]
    }

    def planoDePagamento() {
        Credito credito = sessionStorageService.credito as Credito
        [nome: credito.cliente.nome]
    }

    def simulador() {

    }

    def printSimulador() {

    }

    def printPlano() {

    }

    def printExtrato() {

    }

    def printContrato() {

    }


}
