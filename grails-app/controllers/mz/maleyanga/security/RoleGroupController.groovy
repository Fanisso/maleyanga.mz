package mz.maleyanga.security

import grails.plugin.springsecurity.annotation.Secured
import mz.maleyanga.BasicController

/**
 * RoleGroupController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class RoleGroupController extends BasicController {

    @Secured(['ROLE_ADMIN', 'ROLE_GROUP'])
    def roleGroup() {}
}
