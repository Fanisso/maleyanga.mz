package utilizador

import mz.maleyanga.UtilizadorService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.security.RoleGroup
import mz.maleyanga.security.Utilizador
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList


@Service
class UtilizadorViewModel {
    private ListModelList<RoleGroup> roleGroups
    UtilizadorService utilizadorService
    Utilizador novoUtilizador = new Utilizador()
    private  String filter = ""
    private  @Wire Label info
    private String blue = "color:blues;font-size:12pt"
    private String red = "color:red"
    private ListModelList<Utilizador> utilizadores


    String getFilter() {
        return filter
    }

    @Command
    def updateUtilizador(){
      def result =  utilizadorService.updateUtilizador(novoUtilizador)
        if(result){
            info.value = "Dados actualizados com sucesso!"
            info.style  ="color:blue;font-weight:bold"

        }else {
            info.value = "Erro na gravação dos dados"
            info.style  ="color:blue;font-weight:bold"
        }
    }

    @Command
    @NotifyChange(["novoUtilizador"])
    def addUtilizador(){
        novoUtilizador = new Utilizador()
        info.value = ""
    }
    @Command
    void doSearch() {
        fecharEditor()
        utilizadores.clear()
        List<Utilizador> allItems = utilizadorService.getAll()
        if (filter == null || "".equals(filter)) {
            utilizadores.addAll(allItems)
        } else {
            for (Utilizador item : allItems) {
                if (item.username.toLowerCase()?.indexOf(filter.toLowerCase()) >= 0 ||
                        item?.email?.toString()?.indexOf(filter) >= 0 ||
                        item?.telefone1?.toString()?.indexOf(filter) >= 0 ||
                        item?.telefone2?.toString()?.indexOf(filter) >= 0 ||
                        item?.endereco?.indexOf(filter) >= 0) {
                    utilizadores.add(item)
                }
            }
        }
    }
    ListModelList<Utilizador> getUtilizadores() {
        if(utilizadores==null){
            utilizadores = new ListModelList<Utilizador>(utilizadorService.getAll())
        }
        return utilizadores
    }

    void setFilter(String filter) {
        this.filter = filter
    }

    @Command
    @NotifyChange(["novoUtilizador"])
def setEnabled()
    {
        novoUtilizador.enabled= !novoUtilizador.enabled
    }

    @Command
    @NotifyChange(["novoUtilizador"])
    def setAccountExpired()
    {
        novoUtilizador.accountExpired =!novoUtilizador.accountExpired
    }

    @Command
    @NotifyChange(["novoUtilizador"])
    def setAccountLocked()
    { novoUtilizador.accountLocked=!novoUtilizador.accountLocked
    }

    @Command
    @NotifyChange(["novoUtilizador"])
    def setPasswordExpired()
    {
        novoUtilizador.passwordExpired =!novoUtilizador.passwordExpired
    }



    @Command
    def fecharEditor()
    {

    }


    @Command
    @NotifyChange(["novoUtilizador"])
    def setNovoUtilizador(Utilizador utilizador)
    {
        System.println(utilizador.toString())
        this.novoUtilizador = utilizador
    }

    @Command
    @NotifyChange(['novoUtilizador','utilizadores'])
    def salvarUtilizador()
    {
        info.value = ""
        try {

            if(novoUtilizador.id!=null){
                info.value = "Este utilizador já existe na base de dados!"
                info.style = red
                return

            }
            if(novoUtilizador.username.equals(null)){
                info.value = "Preencha o campo 'Username'!"
                info.style = "color:red"
                return
            }
            if(Cliente.findByNome(novoUtilizador.username)){
                info.value = "Já existe um utilizador com este nome!"
                info.style = "color:red"
                return
            }
            if(Cliente.findById(novoUtilizador.id)){
                info.value = "Este Cliente já existe na base de dados!"
                info.style = "color:red"
                return
            }


            info.value = ""


            def result = utilizadorService.saveUtilizador(novoUtilizador)
            if(result){
                info.value = "Dados gravados com sucesso!"
                info.style  ="color:blue"
                utilizadores.add(novoUtilizador)
                novoUtilizador = new Utilizador()
            }else {
                info.value = "Erro na gravação dos dados"
                info.style  ="color:red"
            }

        } catch (Exception e) {
            info.value = "Erro na gravação dos dados"
            info.style  ="color:red"
            System.println(e.toString())


        }




    }

}
