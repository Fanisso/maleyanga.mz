package mz.maleyanga.settings



import grails.transaction.Transactional
import mz.maleyanga.BasicController
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * SettingsController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class SettingsController extends BasicController {

    def defCredito() {

    }

    static allowedMethods = [save: ["POST", "PUT"], update: ["PUT", "POST"], delete: "DELETE"]

    @Secured(['ROLE_ADMIN', 'SETTINGS_INDEX'])
    def index(Integer max) {
        updateCurrentAction('index')
        params.max = Math.min(max ?: 10, 100)
        respond Settings.list(params), model: [settingsInstanceCount: Settings.count()]
    }







    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'settingsInstance.label', default: 'Settings'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
