package mz.maleyanga.security

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * UtilizadorRoleController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
@ActionLogging
@SpringUserIdentification
@Secured(['ROLE_ADMIN'])
class UtilizadorRoleController extends BasicController {

    static allowedMethods = [save: ["POST", "PUT"], update: ["PUT", "POST"], delete: "DELETE"]







    @Transactional
    @Secured(['ROLE_ADMIN','UTILIZADOR_ROLE_DELETE'])
    def delete(UtilizadorRole utilizadorRoleInstance) {

        if (utilizadorRoleInstance == null) {
            notFound()
            return
        }

        utilizadorRoleInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'UtilizadorRole.label', default: 'UtilizadorRole'), utilizadorRoleInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'utilizadorRoleInstance.label', default: 'UtilizadorRole'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
