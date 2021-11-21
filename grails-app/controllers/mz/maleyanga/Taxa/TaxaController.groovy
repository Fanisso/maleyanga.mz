package mz.maleyanga.Taxa

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import mz.maleyanga.security.Utilizador
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * TaxaController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
@ActionLogging
@SpringUserIdentification
@Secured(['ROLE_ADMIN'])
class TaxaController extends BasicController {
    def springSecurityService
    static allowedMethods = [save: ["POST","PUT"], update: ["PUT","POST"], delete: "DELETE"]
    @Secured(['ROLE_ADMIN', 'TAXA_INDEX'])
    def taxas() {

    }


}
