package mz.maleyanga.pedidoDeCredito

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import mz.maleyanga.security.Utilizador
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * PedidoDeCreditoController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
@ActionLogging
@SpringUserIdentification
class PedidoDeCreditoController extends BasicController {
    def pedidoDeCreditoService
    def springSecurityService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN', 'PEDIDO_DE_CREDITO_LIST_APROVADOS'])
    def pedidoDeCredito() {

    }

    @Secured(['ROLE_ADMIN', 'PEDIDO_DE_CREDITO_LIST_APROVADOS'])
    def listAprovados(Integer max) {
        updateCurrentAction('listAprovados')
        params.max = Math.min(max ?: 10, 100)
        def pedidosAprovados = PedidoDeCredito.findAllByEstado("Aprovado")
        ArrayList pedidosAprova = new ArrayList()
        for (PedidoDeCredito p in pedidosAprovados) {
            if (!p.creditado) {
                pedidosAprova.add(p)
            }
        }


        respond pedidosAprova, model: [pedidoDeCreditoInstanceCount: pedidosAprova.size()]
    }

    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_LIST_FECHADOS'])
    def listFechados(Integer max) {
        updateCurrentAction('listFechados')
        params.max = Math.min(max ?: 10, 100)
        def pedidos = PedidoDeCredito.all
        ArrayList pedidosFechados = new ArrayList()
        for (PedidoDeCredito p in pedidos){
            if(p.creditado||p.estado.equals("Reprovado")){
                pedidosFechados.add(p)
            }
        }

        respond pedidosFechados, model: [pedidoDeCreditoInstanceCount: pedidosFechados.size()]
    }

    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_LIST_PENDENTES'])
    def listPendentes(Integer max) {
        updateCurrentAction('listPendentes')
        params.max = Math.min(max ?: 10, 100)
        def  pedidosPendentes = PedidoDeCredito.findAllByEstado("Pendente")


        respond pedidosPendentes, model: [pedidoDeCreditoInstanceCount: pedidosPendentes.size()]
    }

    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_LIST_ABERTOS'])
    def listAbertos(Integer max) {
        updateCurrentAction('listAbertos')
        params.max = Math.min(max ?: 10, 100)
        def  pedidosAbertos = PedidoDeCredito.findAllByEstado("Aberto")


        respond pedidosAbertos, model: [pedidoDeCreditoInstanceCount: pedidosAbertos.size()]
    }

    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_LIST_INDEX'])
    def index(Integer max) {
        updateCurrentAction('index')
        params.max = Math.min(max ?: 10, 100)
        respond PedidoDeCredito.list(params), model: [pedidoDeCreditoInstanceCount: PedidoDeCredito.count()]
    }

    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_LIST_ALL'])
    def listAll(Integer max) {
        updateCurrentAction('index')
        params.max = Math.min(max ?: 10, 100)
        respond PedidoDeCredito.list(params), model: [pedidoDeCreditoInstanceCount: PedidoDeCredito.count()]
    }


    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_LIST'])
    def list(Integer max) {
        updateCurrentAction('list')
        params.max = Math.min(max ?: 10, 100)
        respond PedidoDeCredito.list(params), model: [pedidoDeCreditoInstanceCount: PedidoDeCredito.count()]
    }


    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_SHOW'])
    def show(PedidoDeCredito pedidoDeCreditoInstance) {
        updateCurrentAction('show')
        respond pedidoDeCreditoInstance
    }


    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_CREATE'])
    def create() {
        updateCurrentAction('create')
        respond new PedidoDeCredito(params)
    }

    @Transactional
    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_SAVE'])
    def save(PedidoDeCredito pedidoDeCreditoInstance) {
        def utilizador = Utilizador.findById(springSecurityService.principal.id)
        pedidoDeCreditoInstance.setUtilizador(utilizador)
        if (pedidoDeCreditoInstance == null) {
            notFound()
            return
        }

        if (pedidoDeCreditoInstance.hasErrors()) {
            respond pedidoDeCreditoInstance.errors, view: 'create'
            return
        }

        def uploadedFile = request.getFile('attachment')
        pedidoDeCreditoInstance.attachment = uploadedFile?.getBytes() //converting the file to bytes
        pedidoDeCreditoInstance.fileName=  uploadedFile.originalFilename //getting the file name from the uploaded file
        pedidoDeCreditoInstance.fileType = uploadedFile.contentType//getting and storing the file type

        pedidoDeCreditoInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pedidoDeCreditoInstance.label', default: 'PedidoDeCredito'), pedidoDeCreditoInstance.id])
                redirect pedidoDeCreditoInstance
            }
            '*' { respond pedidoDeCreditoInstance, [status: CREATED] }
        }
        flash.message = "O pedido No."+pedidoDeCreditoInstance.id+" foi criado com sucesso!"
        redirect(controller: "pedidoDeCredito", action: "show", id: pedidoDeCreditoInstance.id)
    }

    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_EDIT'])
    def edit(PedidoDeCredito pedidoDeCreditoInstance) {
        updateCurrentAction('edit')
        respond pedidoDeCreditoInstance
    }

    @Transactional
    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_UPDATE'])
    def update(PedidoDeCredito pedidoDeCreditoInstance) {
        def utilizador = Utilizador.findById(springSecurityService.principal.id)
        pedidoDeCreditoInstance.setUtilizador(utilizador)
        if (pedidoDeCreditoInstance == null) {
            notFound()
            return
        }

        if (pedidoDeCreditoInstance.hasErrors()) {
            respond pedidoDeCreditoInstance.errors, view: 'edit'
            return
        }

        pedidoDeCreditoInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'PedidoDeCredito.label', default: 'PedidoDeCredito'), pedidoDeCreditoInstance.id])
                redirect pedidoDeCreditoInstance
            }
            '*' { respond pedidoDeCreditoInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_ADMIN','PEDIDO_DE_CREDITO_DELETE'])
    def delete(PedidoDeCredito pedidoDeCreditoInstance) {

        if (pedidoDeCreditoInstance == null) {
            notFound()
            return
        }

        pedidoDeCreditoInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'PedidoDeCredito.label', default: 'PedidoDeCredito'), pedidoDeCreditoInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'pedidoDeCreditoInstance.label', default: 'PedidoDeCredito'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }


    def showAttachment(PedidoDeCredito pedidoDeCreditoInstance){

        //retrieve photo code here
        response.setHeader("Content-disposition", "attachment; filename=${pedidoDeCreditoInstance.fileName}")
        response.contentType = pedidoDeCreditoInstance.fileType //'image/jpeg' will do too
        response.outputStream << pedidoDeCreditoInstance.attachment //'myphoto.jpg' will do too
       /* response.outputStream.flush()*/


    }

}
