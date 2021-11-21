package mz.maleyanga.conta



import grails.transaction.Transactional
import mz.maleyanga.BasicController
import mz.maleyanga.SessionStorageService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.entidade.Entidade
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * ContaController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
class ContaController extends BasicController {
    def sessionStorageService
    static allowedMethods = [save: ["POST", "PUT"], update: ["PUT", "POST"], delete: "DELETE"]

    def printExtratoDeConta() {
        Conta conta = sessionStorageService.getConta() as Conta
        [conta: conta.designacaoDaConta]
    }


    def contas() {

    }

    def printExtrato() {

    }




}
