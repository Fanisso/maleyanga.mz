package mz.maleyanga.entidade

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * EntidadeController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
@ActionLogging
@SpringUserIdentification
class EntidadeController extends BasicController {

    static allowedMethods = [save: ["POST","PUT"], update: ["PUT","POST"], delete: "DELETE"]
    @Secured(['ROLE_ADMIN','ENTIDADE_INDEX'])
	def index(Integer max) {

        updateCurrentAction('index')
        params.max = Math.min(max ?: 10, 100)
        respond Entidade.list(params), model:[entidadeInstanceCount: Entidade.count()]
    }

    @Secured(['ROLE_ADMIN','ENTIDADE_SELECT'])
    def select(Integer max) {
        updateCurrentAction('select')
        params.max = Math.min(max ?: 10, 100)
        respond Entidade.list(params), model:[entidadeInstanceCount: Entidade.count()]
    }


    @Secured(['ROLE_ADMIN', 'ENTIDADE_LIST'])
    def list(Integer max) {
        updateCurrentAction('list')
        params.max = Math.min(max ?: 10, 100)
        respond Entidade.list(params), model: [entidadeInstanceCount: Entidade.count()]
    }

    @Secured(['ROLE_ADMIN', 'ENTIDADE_SHOW'])
    def contrato() {

    }

    @Secured(['ROLE_ADMIN', 'ENTIDADE_SHOW'])
    def show(Entidade entidadeInstance) {
        session.setAttribute('entidade', entidadeInstance)
        updateCurrentAction('show')
        respond entidadeInstance
    }





}
