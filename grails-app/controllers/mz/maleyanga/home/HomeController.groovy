package mz.maleyanga.home

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import mz.maleyanga.BasicController
import mz.maleyanga.SessionStorageService

/**
 * HomeController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class HomeController extends BasicController {
    SessionStorageService sessionStorageService

    def index = {
        String blank = ""
        def s_blank = sessionStorageService.getBlank()
        if (s_blank) {
            blank = s_blank
        }
        [blank: blank]
    }


}
