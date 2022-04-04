package relatorios

import mz.maleyanga.ClienteDetalhado
import mz.maleyanga.ClienteService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import org.zkoss.zul.ListModelList


class PrintClientesViewModel {
    ClienteService clienteService
    ListModelList<Cliente> clientes = new ListModelList<Cliente>(clienteService.all())

    ListModelList<Cliente> getClientes() {
        if(clientes==null){
            clientes =  new ListModelList<Cliente>()
        }
        clientes.clear()
        clientes = clienteService.all()
        return clientes
    }
}
