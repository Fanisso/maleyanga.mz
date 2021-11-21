package mz.maleyanga.diario


import grails.transaction.Transactional
import mz.maleyanga.BasicController
import mz.maleyanga.SessionStorageService
import mz.maleyanga.security.Utilizador
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*


class DiarioController extends BasicController {
    def sessionStorageService

    @Secured(['ROLE_ADMIN', 'DIARIO_EDIT'])
    def diario() {}

    def printDi() {}

    def printUser() {}

    @Secured(['ROLE_ADMIN', 'DIARIO_EDIT'])
    def printDiario() {
        // render(view: "printDiario", target: "_blank")
        [diario: sessionStorageService.diario]

    }

    @Secured(['ROLE_ADMIN', 'DIARIO_EDIT'])
    def printDiarioPorUtilizador() {
        [diario: sessionStorageService.diario, username: sessionStorageService.utilizador]
    }
}
