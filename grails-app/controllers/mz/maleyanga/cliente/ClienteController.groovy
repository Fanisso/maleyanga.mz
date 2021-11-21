package mz.maleyanga.cliente

import grails.transaction.Transactional
import mz.maleyanga.BasicController

import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * ClienteController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
@ActionLogging
@SpringUserIdentification
class ClienteController extends BasicController {
    @Secured(['ROLE_ADMIN', 'ROLE_ASSIS_ADMIN', 'CLIENTE_GESTOR'])
    def gestorDeClientes() {

    }



    @Secured(['ROLE_ADMIN', 'ROLE_ASSIS_ADMIN', 'CLIENTE_GESTOR'])
    def novoCliente() {

    }

}
