package mz.maleyanga.logs

import grails.plugin.springsecurity.annotation.Secured
import mz.maleyanga.BasicController

/**
 * LogsController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Secured(['ROLE_ADMIN','ROLE_GESTOR','ROLE_TESO','ROLE_USER'])
class LogsController extends BasicController {
    @org.springframework.security.access.annotation.Secured(['ROLE_ADMIN','LOGS_INDEX'])
    def index(){}

    // static scaffold = true
//	def index = { }
}
