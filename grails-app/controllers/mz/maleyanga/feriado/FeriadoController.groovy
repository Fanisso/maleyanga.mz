package mz.maleyanga.feriado

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import mz.maleyanga.BasicController

/**
 * FeriadoController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class FeriadoController extends BasicController{

    @Secured(['ROLE_ADMIN','FERIADO_FERIADO_CRUD'])
    def feriadoCrud(Integer max) {
        updateCurrentAction('index')
        params.max = Math.min(max ?: 10, 100)
        respond Feriado.list(params), model: [feriadoInstanceCount: Feriado.count()]
    }



}
