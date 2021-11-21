package cliente

import mz.maleyanga.ClienteService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.security.Utilizador
import org.zkoss.zk.grails.*

import org.zkoss.zul.ListModelList

class CbClientesViewModel {
    SessionStorageService sessionStorageService
    private ListModelList<Cliente> clientes
    private Cliente selectedCliente
    private Utilizador selectedGestor
    ClienteService clienteService
    Cliente getSelectedCliente() {
        return selectedCliente
    }

    Utilizador getSelectedGestor() {
        if(sessionStorageService.getSelectedGestor()!=null){
            selectedGestor = sessionStorageService.getSelectedGestor() as Utilizador
        }
        return selectedGestor
    }

    void setSelectedCliente(Cliente selectedCliente) {
        this.selectedCliente = selectedCliente
        sessionStorageService.cliente = selectedCliente
    }

    ListModelList getClientes() {
        if(clientes==null){
            clientes = new ListModelList<Cliente>()
        }
        clientes.clear()
        clientes = clienteService.findAllByAtivo()
        return clientes
    }
    ListModelList getClientes(Utilizador selectedGestor) {
        if(clientes==null){
            clientes = new ListModelList<Cliente>()
        }
        clientes.clear()
        clientes = Cliente.findAllByUtilizador(selectedGestor)
        return clientes
    }


}
