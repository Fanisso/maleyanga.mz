package mz.maleyanga.security

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * RoleController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
@ActionLogging
@SpringUserIdentification
@Secured(['ROLE_ADMIN'])
class RoleController extends BasicController {

    static allowedMethods = [save: ["POST","PUT"], update: ["PUT","POST"], delete: "DELETE"]

    @Secured(['ROLE_ADMIN','ROLE_INDEX'])
	def index(Integer max) {
        updateCurrentAction('index')
        params.max = Math.min(max ?: 10, 100)
        respond Role.list(params), model:[roleInstanceCount: Role.count()]
    }

    @Secured(['ROLE_ADMIN','ROLE_LIST'])
	def list(Integer max) {
        updateCurrentAction('list')
        params.max = Math.min(max ?: 10, 100)
        respond Role.list(params), model:[roleInstanceCount: Role.count()]
    }

    @Secured(['ROLE_ADMIN','ROLE_SHOW'])
    def show(Role roleInstance) {
        updateCurrentAction('show')
        respond roleInstance
    }

    @Secured(['ROLE_ADMIN','ROLE_CREATE'])
    def create() {
        updateCurrentAction('create')
        respond new Role(params)
    }

    @Transactional
    @Secured(['ROLE_ADMIN','ROLE_SAVE'])
    def save(Role roleInstance) {
        if (roleInstance == null) {
            notFound()
            return
        }

        if (roleInstance.hasErrors()) {
            respond roleInstance.errors, view:'create'
            return
        }

        roleInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'roleInstance.label', default: 'Role'), roleInstance.id])
                redirect roleInstance
            }
            '*' { respond roleInstance, [status: CREATED] }
        }
    }

    def edit(Role roleInstance) {
        updateCurrentAction('edit')
        respond roleInstance
    }

    @Transactional
    @Secured(['ROLE_ADMIN','ROLE_UPDATE'])
    def update(Role roleInstance) {
        if (roleInstance == null) {
            notFound()
            return
        }

        if (roleInstance.hasErrors()) {
            respond roleInstance.errors, view:'edit'
            return
        }

        roleInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Role.label', default: 'Role'), roleInstance.id])
                redirect roleInstance
            }
            '*'{ respond roleInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_ADMIN','ROLE_DELETE'])
    def delete(Role roleInstance) {

        if (roleInstance == null) {
            notFound()
            return
        }

        roleInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Role.label', default: 'Role'), roleInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'roleInstance.label', default: 'Role'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
