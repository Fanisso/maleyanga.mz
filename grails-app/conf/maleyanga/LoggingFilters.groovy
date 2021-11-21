package maleyanga

import grails.plugin.springsecurity.SpringSecurityService

/**
 * LoggingFilters
 * A filters class is used to execute code before and after a controller action is executed and also after a view is rendered
 */
class LoggingFilters {
    SpringSecurityService springSecurityService
    def filters = {
        all(controller:'*', action:'*') {
            before = {

            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
