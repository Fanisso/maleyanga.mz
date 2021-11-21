package relatorios


import mz.maleyanga.Gestor
import mz.maleyanga.RelatoriosService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.UtilizadorService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.security.Utilizador
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList


@Service
class ClientesViewModel {
    private ListModelList<Utilizador> gestores
    private List<Cliente> clientes
    private List<Cliente> todosClientes
    private List<Cliente> inativos
    private List<Cliente> ativos
    private List<Cliente> semGestores
    private List<Gestor> gestors
    private  Gestor gestor
    SessionStorageService sessionStorageService
    private  Utilizador selectedGestor
    private  Cliente selectedCliente
    UtilizadorService utilizadorService
    String lb_tb_clientes = ""
    String lb_tb_creditos = ""
    int totalClientes
    boolean abertos = false
    boolean emPagamento = false
    boolean pendentes = false
    boolean fechados = false
    boolean invalidos = false
    Date ano
    RelatoriosService relatoriosService

    Date getAno() {

        return ano
    }

    void setAno(Date ano) {
        this.ano = ano
        relatoriosService.ano = ano
    }

    boolean getAbertos() {
        return abertos
    }

    @Command
    @NotifyChange(["abertos"])
    void setA() {
        this.abertos = !abertos
        relatoriosService.abertos =abertos
        System.println(abertos)
    }

    boolean getEmPagamento() {
        return emPagamento
    }

    @Command
    @NotifyChange(["emPagamento"])
    void setE() {
        this.emPagamento = !emPagamento
        relatoriosService.emPagamento =emPagamento
    }

    boolean getPendentes() {
        return pendentes
    }

    @Command
    @NotifyChange(["pendentes"])
    void setP() {
        this.pendentes = !pendentes
        relatoriosService.pendentes =pendentes
    }

    boolean getFechados() {
        return fechados
    }

    @Command
    @NotifyChange(["fechados"])
    void setF() {
        this.fechados = !fechados
        relatoriosService.fechados =fechados
    }

    boolean getInvalidos() {
        return invalidos
    }

    @Command
    @NotifyChange(["invalidos"])
    void setI() {
        invalidos=!invalidos
        relatoriosService.invalidos =invalidos
    }

    String getLb_tb_creditos() {
        return lb_tb_creditos
    }

    List<Cliente> getSemGestores() {
        if(semGestores == null){
            semGestores = new ListModelList<Cliente>(Cliente.findAllByUtilizadorIsNull())
        }
        return semGestores
    }

    Gestor getGestor() {
        return gestor
    }

    @NotifyChange(["clientes","lb_tb_clientes","clientes","ativos","inativos","selectedGestor"])
    void setGestor(Gestor gestor) {
        selectedGestor = Utilizador.findByUsername(gestor.nome)
        this.gestor = gestor
        lb_tb_clientes=getClientes().size()+" Clientes-"+selectedGestor.username
        sessionStorageService.selectedGestor = selectedGestor
    }

    List<Gestor> getGestors() {
        if(gestors==null){
            gestors = new ListModelList<Gestor>()
        }
        gestors.clear()

        for(Utilizador utilizador in  gestores){
            Gestor gestor = new Gestor()
            gestor.nome = utilizador.username
            gestor.ativos = Cliente.findAllByUtilizadorAndEmDivida(utilizador,true).size()
            gestor.inativos = Cliente.findAllByUtilizadorAndEmDivida(utilizador,false).size()
            gestors.add(gestor)
        }
        return gestors
    }

     int getTotalClientes() {
        totalClientes = Cliente.findAllByAtivo(true).size()
        return totalClientes
    }

    Cliente getSelectedCliente() {
        return selectedCliente
    }

    @NotifyChange(["creditos","lb_tb_creditos"])
    void setSelectedCliente(Cliente selectedCliente) {
        this.selectedCliente = selectedCliente
    }

    List<Cliente> getInativos() {
        if(inativos==null){
            inativos = new ListModelList<Cliente>()
        }
        inativos.clear()
        if(selectedGestor){
            inativos = Cliente.findAllByUtilizadorAndEmDivida(selectedGestor,false)
        }
        return inativos
    }

    List<Cliente> getAtivos() {
        if(ativos==null){
            ativos = new ListModelList<Cliente>()
        }
        ativos.clear()
        if(selectedGestor){
            ativos = Cliente.findAllByUtilizadorAndEmDivida(selectedGestor,true)
        }
        return ativos
    }

    String getLb_tb_clientes() {

        return lb_tb_clientes
    }

    List<Cliente> getTodosClientes() {
        if(todosClientes==null){
            todosClientes = new ArrayList<Cliente>()
        }
        todosClientes.clear()
        todosClientes = Cliente.all
        return todosClientes
    }

    ListModelList<Cliente> getClientes() {
        if(clientes==null){
            clientes = new ArrayList<Cliente>()
        }
        clientes.clear()
        if(selectedGestor){
            clientes = Cliente.findAllByUtilizador(selectedGestor)
        }
        return clientes
    }



    @NotifyChange(["clientes","lb_tb_clientes","clientes","ativos","inativos","gestor"])
    void setSelectedGestor(Utilizador selectedGestor) {
        this.selectedGestor = selectedGestor
        lb_tb_clientes=getClientes().size()+" Clientes-"+selectedGestor.username
        gestor = gestors.find {selectedGestor.username}
    }

    Utilizador getSelectedGestor() {
        return selectedGestor
    }

    ListModelList getGestores() {
        if (gestores==null){
            gestores = new ListModelList<Utilizador>()
        }
        gestores.clear()
        def utilizadores = utilizadorService.getAll()
        for(Utilizador u in utilizadores){
            if (u.authorities.any { it.authority == "CLIENTE_GESTOR" }&&!u.clientes.empty) {
                gestores.add(u)
            }
        }
        return gestores
    }
    @Init init() {
        // initialzation code here
        getGestores()
    }



}
