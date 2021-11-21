package relatorios

import mz.maleyanga.ClienteService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.UtilizadorService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.security.Utilizador
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList


@Service
class PrestacoesViewModel {
    @Wire Label info
    SessionStorageService sessionStorageService
    UtilizadorService utilizadorService
    ClienteService clienteService
    private ListModelList gestores
    private ListModelList clientes
    private ListModelList creditos
    private  Utilizador selectedGestor
    private  Cliente selectedCliente
    private String filterCliente
    private Credito credito
    Date dataInicial
    Date dataFinal
    boolean exluirPagos = true
    boolean exluirMoras = true

    Credito getCredito() {
        return credito
    }

    @NotifyChange(["credito","selectedCliente"])
    void setCredito(Credito credito) {
        this.credito = credito
        sessionStorageService.setCredito(credito)
    }

    ListModelList getCreditos() {
        if(creditos==null){
            creditos = new ListModelList()
        }
        creditos.clear()
        if(selectedCliente){
            creditos = Credito.findAllByCliente(selectedCliente)
        }
        return creditos
    }

    String getFilterCliente() {
        return filterCliente
    }

    void setFilterCliente(String filterCliente) {
        this.filterCliente = filterCliente
    }

    @Command
    @NotifyChange(["clientes","selectedCliente","pagamentos","selectedCredito","creditos","selectedPagamento","filterCliente"])
    void doSearchCliente() {
        info.value=""
        clientes.clear()
        if(filterCliente.contains("/")){
            credito = Credito.findByNumeroDoCredito(filterCliente)
            if(credito){
                selectedCliente = credito.cliente
                return
            }else {

                info.value+="Crédito não indentificado !"
                info.style = "color:red;font-weight;font-size:16px;background:back"
            }
        }

        List<Cliente> allItems = clienteService.findAllByName(filterCliente)
        if (filterCliente != null &&! "".equals(filterCliente))
        {
            for (Cliente item : allItems) {
                if (item.nome.toLowerCase().indexOf(filterCliente.toLowerCase()) >= 0 ||

                        item.numeroDeIndentificao.indexOf(filterCliente) >= 0) {
                    clientes.add(item)

                }
            }
            if(clientes.empty){

                info.value+="Cliente não indentificado !"
                info.style = "color:red;font-weight;font-size:16px;background:back"
            }
        }
    }



    Cliente getSelectedCliente() {
        return selectedCliente
    }

    @NotifyChange(["creditos"])
    void setSelectedCliente(Cliente selectedCliente) {
        this.selectedCliente = selectedCliente
        sessionStorageService.setCliente(selectedCliente)
    }

    Date getDataInicial() {
        return dataInicial
    }

    @NotifyChange(["dataFinal","dataInicial"])
    void setDataInicial(Date dataInicial) {
        Calendar c = Calendar.getInstance()
        c.setTime(dataInicial)
        c.add(Calendar.MONTH, 1)
        c.add(Calendar.DAY_OF_MONTH,-1)
        dataFinal = c.getTime()
        this.dataInicial = dataInicial
        sessionStorageService.dataInicial = dataInicial
        sessionStorageService.dataFinal = dataFinal

    }

    boolean getExluirMoras() {
        sessionStorageService.setExluirMoras(exluirMoras)
        return exluirMoras
    }

    @NotifyChange(["exluirPagos"])
    @Command
    void excluir() {
        this.exluirPagos =!exluirPagos
        sessionStorageService.setExluirPagos(exluirPagos)
    }

    @NotifyChange(["exluirPagos"])
    @Command
    void excluirMoras() {
        this.exluirMoras =!exluirMoras
        sessionStorageService.setExluirMoras(exluirMoras)
    }


    boolean getExluirPagos() {
        sessionStorageService.setExluirPagos(exluirPagos)
        return exluirPagos
    }

    Date getDataFinal() {
        return dataFinal
    }

    void setDataFinal(Date dataFinal) {
        if(dataFinal<dataInicial){
            Calendar c = Calendar.getInstance()
            c.setTime(dataInicial)
            c.add(Calendar.DAY_OF_MONTH, 1)
            dataFinal = c.getTime()
            this.dataFinal = dataFinal

        }else {
            this.dataFinal = dataFinal
        }

        sessionStorageService.dataFinal= dataFinal
    }

    Utilizador getSelectedGestor() {
        return selectedGestor
    }

    void setSelectedGestor(Utilizador selectedGestor) {
        this.selectedGestor = selectedGestor
        sessionStorageService.selectedGestor = selectedGestor
    }

    ListModelList getGestores() {
        if (gestores==null){
            gestores = new ListModelList<Utilizador>()
        }
        gestores.clear()
        def utilizadores = utilizadorService.all
        for(Utilizador u in utilizadores){
            if (u.authorities.any { it.authority == "CLIENTE_GESTOR" }) {
                gestores.add(u)
            }
        }
        return gestores
    }

    ListModelList getClientes() {
        if (clientes==null){
            clientes = new ListModelList<Cliente>()
        }
        return clientes
    }
}
