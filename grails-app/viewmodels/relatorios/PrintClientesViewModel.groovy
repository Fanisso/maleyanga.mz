package relatorios

import mz.maleyanga.ClienteDetalhado
import mz.maleyanga.ClienteService
import mz.maleyanga.cliente.Cliente
import org.zkoss.zul.ListModelList


class PrintClientesViewModel {
    ClienteService clienteService
    ListModelList<ClienteDetalhado> clienteDetalhados

    ListModelList<ClienteDetalhado> getClienteDetalhados() {
        if(clienteDetalhados==null){
            clienteDetalhados = new ListModelList<ClienteDetalhado>()
        }
        def clientes = clienteService.all()
        for(Cliente cliente in clientes){
            ClienteDetalhado clienteDetalhado = new ClienteDetalhado(nome: cliente.nome,
            contacto: cliente.telefone,residencia: cliente.residencia,ativo: cliente.ativo.toString(),
            classificacao: cliente.classificacao,qtdDeCreditos: cliente.creditos.size(),
                    creditos: cliente.creditos,valorEmDivida: cliente.totalEmDivida,gestor: cliente.utilizador.username)
            clienteDetalhados.add(clienteDetalhado)
        }
        return clienteDetalhados
    }
}
