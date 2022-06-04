package home

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import mz.maleyanga.SessionStorageService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.conta.Conta
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.grails.artefacts.CometArtefactHandler
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Window

import javax.swing.ListModel
@Service
class IndexViewModel {
    SpringSecurityService springSecurityService
    SessionStorageService sessionStorageService
    String message
    @Wire  btnHello
    @Wire  Window win_home
    List clientesEmDivida
    Cliente cliente
    boolean blank = true




    @NotifyChange(["blank"])
    @Command
    def change(){
    blank= !blank
        if(blank){
            sessionStorageService.setBlank("_blank")
        }else {
            sessionStorageService.setBlank("")
        }
    }

    boolean getBlank() {
        def s_blank = sessionStorageService.getBlank()
        return s_blank == "_blank"

    }

    @NotifyChange(["blank"])
    void setBlank(boolean blank) {
        this.blank != blank
    }

    List getClientesEmDivida() {
        if (clientesEmDivida ==null){
            clientesEmDivida = new ListModelList<Cliente>()
        }

        return clientesEmDivida
    }

    @Init init() {
        if (!springSecurityService.isLoggedIn()) {
            Executions.sendRedirect("/login/auth")
        }

    }

    @Command
    @NotifyChange(["cliente"])
    def showDetails(){
        cliente = Cliente.findById(cliente.id)
    }
    @NotifyChange(['message'])
    @Command clickMe() {
        message = "Clicked"
    }

    @NotifyChange(["cliente"])
    void setCliente(Cliente cliente) {

        this.cliente = cliente
    }

    @Command
    def  showNovoCliente(){
        Executions.sendRedirect("/cliente/create.gsp/")
    }

    @Command
    def  showClientes(){
        Executions.sendRedirect("/cliente/gestorDeClientes/")
    }

    Cliente getCliente() {

        return cliente
    }
     @Command
    def closeWin(){
         Messagebox.show("wind closed")
         System.println("close win")
     }


}
