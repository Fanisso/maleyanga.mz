package mz.maleyanga.security

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * UtilizadorController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
@ActionLogging
@SpringUserIdentification
@Secured(['ROLE_ADMIN'])
class UtilizadorController extends BasicController {

    static allowedMethods = [save: ["POST", "PUT"], update: ["PUT", "POST"], delete: "DELETE"]

    @grails.plugin.springsecurity.annotation.Secured(['ROLE_ADMIN','UTILIZADOR_INDEX'])
    def index(Integer max) {
        updateCurrentAction('index')
        params.max = Math.min(max ?: 10, 100)
        respond Utilizador.list(params), model: [utilizadorInstanceCount: Utilizador.count()]
    }
    def logs(){}

    @grails.plugin.springsecurity.annotation.Secured(['ROLE_ADMIN','UTILIZADOR_LIST'])
    def list(Integer max) {
        updateCurrentAction('list')
        params.max = Math.min(max ?: 10, 100)
        respond Utilizador.list(params), model: [utilizadorInstanceCount: Utilizador.count()]
    }




    @Secured(['ROLE_ADMIN', 'UTILIZADOR_UTILIZADOR_CRUD'])
    def utilizadorCrud() {}

    @Secured(['ROLE_ADMIN', 'UTILIZADOR_UTILIZADOR_CRUD'])
    def utilizador() {}

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'utilizadorInstance.label', default: 'Utilizador'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
