package mz.maleyanga.simulador

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * SimuladorController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
@ActionLogging
@SpringUserIdentification
@Secured(['ROLE_ADMIN','ROLE_ASSIS_ADMIN','ROLE_GESTOR_CREDITO'])
class SimuladorController extends BasicController {




    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'simuladorInstance.label', default: 'Simulador'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
