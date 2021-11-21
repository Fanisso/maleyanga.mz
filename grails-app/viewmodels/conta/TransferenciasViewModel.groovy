package conta

import grails.transaction.Transactional
import mz.maleyanga.TransferenciaService
import mz.maleyanga.conta.Conta
import mz.maleyanga.transferencia.Transferencia
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.GlobalCommand
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList

@Service
@Transactional
class TransferenciasViewModel {
    @Wire Label info
    private String styleRed = "color:red;font-size:16px;font-weight"
    private String styleBlue = "color:blue;font-size:16px;font-weight"
    private String filterOrigens
    private String filterDestinos
    private  Conta selectedContaOrigem
    private  Conta selectedContaDestino
    private ListModelList<Conta> contaorigens
    private ListModelList<Conta> contadestinos
    TransferenciaService transferenciaService
    private ListModelList<Transferencia> transferencias
    private ListModelList<Transferencia> trans

    String getFilterDestinos() {
        return filterDestinos
    }

    void setFilterDestinos(String filterDestinos) {
        this.filterDestinos = filterDestinos
    }
    @GlobalCommand
    @NotifyChange(["contaorigens"])
    ListModelList<Conta> getContaorigens() {
        if (contaorigens == null) {
            contaorigens = new ArrayList<Conta>()
        }
        def contas = new ListModelList<Conta>(Conta.all)
        for (Conta conta in contas) {
            if (conta.finalidade == "conta_movimento" || conta.finalidade == "conta_capital" || conta.finalidade == "conta_caixa" || conta.finalidade == "fundo_de_maneio") {
                contaorigens.add(conta)
            }

        }
        return contaorigens
    }
    @GlobalCommand
    @NotifyChange(["contadestinos"])
    ListModelList<Conta> getContadestinos() {
        if (contadestinos == null) {
            contadestinos = new ListModelList<>()
        }
        def contas = new ListModelList<Conta>(Conta.all)
        for (Conta conta in contas) {
            if (conta.finalidade == "conta_movimento" || conta.finalidade == "conta_capital" || conta.finalidade == "conta_caixa" || conta.finalidade == "fundo_de_maneio") {
                contadestinos.add(conta)
            }

        }

        return contadestinos
    }
    ListModelList<Transferencia> getTransferencias() {
        if(transferencias==null){
            transferencias = new ListModelList<Transferencia>()
        }
        return transferencias
    }

    ListModelList<Transferencia> getTrans() {
        if(trans == null){
            trans = new ListModelList<Transferencia>()
        }
        return trans
    }

    String getFilterOrigens() {
        return filterOrigens
    }

    void setFilterOrigens(String filterOrigens) {
        this.filterOrigens = filterOrigens
    }


    @Command
    @NotifyChange([''])
    def limpar(){
        transferencias.clear()
    }
    @Command
    @NotifyChange(['transferencias',"selectedContaDestino","selectedContaOrigem","trans"])
    def addDestino(){
        if(selectedContaOrigem==null){
            info.value = "Selecione a conta Origem"
            info.style = styleRed
        }else {
            Transferencia transferencia = new Transferencia()
            transferencia.origem = selectedContaOrigem
            transferencia.destino = selectedContaDestino
            transferencias.add(transferencia)
        }
    }
    @Command
    @NotifyChange(['info'])
    def verificarCondicoes(){
        for(Transferencia tr in transferencias){
            if(selectedContaOrigem.saldo<tr.valor){
                info.value = "A conta '"+selectedContaOrigem.designacaoDaConta+"' não tem saldo sufuciente!"
                info.style = styleRed
            }
        }
    }
    @Command
    @NotifyChange(['contaorigens','contadestinos',"transferencias",'contas','cis','integradoras','trans',"selectedContaDestino","selectedContaOrigem","trans"])
    def salvarTrans(){
        trans = new ListModelList<Transferencia>()
        info.value = ""
        try {
            if(transferencias.empty){
                info.value = "Selecione as contas e o valor!"
                info.style = styleRed
                return
            }
                for(Transferencia t in transferencias){
                    System.println(t.valor)
                }

            boolean ok=  transferenciaService.salvarTransferencias(transferencias)
            if (ok){
                info.value = "Os dados foram gravados com sucesso !"
                info.style = styleBlue
                for(Transferencia t in transferencias){
                    trans.add(Transferencia.findById(t.id))
                }
                transferencias.clear()
            }else {
                info.value = "Erro na gravação dos dados !"
                info.style = styleRed

            }
            selectedContaOrigem.getSaldo()
            selectedContaDestino.getSaldo()
            info.value = "Dados gravados com sucesso!"
            info.style = styleBlue
            getTrans()
        }catch(Exception e) {
            System.println(e.toString())
        }

    }
    Conta getSelectedContaOrigem() {
        return selectedContaOrigem
    }

    @NotifyChange(["selectedContaOrigem","selectedContaDestino"])
    void setSelectedContaOrigem(Conta selectedContaOrigem) {
        this.selectedContaOrigem = selectedContaOrigem
    }
    Conta getSelectedContaDestino() {
        return selectedContaDestino
    }
    @NotifyChange(["selectedContaOrigem","selectedContaDestino"])
    void setSelectedContaDestino(Conta selectedContaDestino) {
        this.selectedContaDestino = selectedContaDestino
    }
    @Command
    @NotifyChange(["filterOrigens","contaorigens"])
    public void doSearchOrigens() {
        info.value = ""
        contaorigens.clear()
        List<Conta> allItems = getOrigens()
        if (filterOrigens == null || "".equals(filterOrigens)) {
            contaorigens.addAll(allItems)
        } else {
            for (Conta item : allItems) {
                if (item.designacaoDaConta.toString().toLowerCase().indexOf(filterOrigens.toLowerCase()) >= 0 ||
                        item.numeroDaConta.toString().indexOf(filterOrigens) >= 0 ||
                        item.codigo.toLowerCase().indexOf(filterOrigens) >= 0) {
                    contaorigens.add(item)
                }

            }
        }
    }
    @Command
    @NotifyChange(["filterDestinos", "contadestinos"])
    public void doSearchDestino() {
        info.value = ""
        contadestinos.clear()
        List<Conta> allItems = getOrigens()
        if (filterDestinos == null || "".equals(filterDestinos)) {
            contadestinos.addAll(allItems)
        } else {
            for (Conta item : allItems) {
                if (item.designacaoDaConta.toLowerCase().indexOf(filterDestinos.toLowerCase()) >= 0 ||
                        item?.numeroDaConta?.indexOf(filterDestinos) >= 0 ||
                        item?.codigo?.indexOf(filterDestinos) >= 0) {
                    contadestinos.add(item)
                }
            }
        }
    }
    List<Conta> getOrigens() {
        def contas = new ListModelList<Conta>(Conta.all)
        List<Conta> origens = new ArrayList<Conta>()
        for (Conta conta in contas) {
            if (conta.finalidade == "conta_movimento" || conta.finalidade == "conta_capital" || conta.finalidade == "conta_caixa" || conta.finalidade == "fundo_de_maneio") {
                origens.add(conta)
            }

        }
        return origens
    }
}
