package pagamento

import mz.maleyanga.diario.Diario
import mz.maleyanga.pagamento.Pagamento
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList

class PagamentosEfetivadosViewModel {

    @Wire Label info
    private  String filter
    private  Long id
    private  Pagamento selectedPagamento
    private ListModelList<Pagamento> pagamentos
    Diario diario
    @Init init() {
      diario = Diario.findByEstado("aberto")
        if (diario==null){
            info.value= "Abra um novo diário para poder lancar pagamentos!"

        }
    }
    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Pagamento getSelectedPagamento() {
        return selectedPagamento
    }

    void setSelectedPagamento(Pagamento selectedPagamento) {
        this.selectedPagamento = selectedPagamento
    }

    String getFilter() {
        return filter
    }

    void setFilter(String filter) {
        this.filter = filter
    }

    @Command
    def  showSelectedPagamento(){

        Executions.sendRedirect("/pagamento/show/"+selectedPagamento.id)
    }

    @Command
    findItem(){
        selectedPagamento = Pagamento.findById(id)
        if(selectedPagamento!=null){
            Executions.sendRedirect("/pagamento/show/"+selectedPagamento.id)
        }
    }


    ListModelList<Pagamento> getPagamentos() {
        if(pagamentos==null){
            pagamentos = new ArrayList<>()
        }
        return pagamentos
    }

    @NotifyChange(["pagamentos","info"])
    @Command
    void doSearch() {
        info.value = ""
        pagamentos.clear()
        List<Pagamento> allItems = Pagamento.findAllWhere(pago: true)
        if (filter == null || "".equals(filter)) {
            pagamentos.addAll(allItems)
        } else {
            for (Pagamento item : allItems) {
                if (item.dataPrevistoDePagamento.toString().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.getId().toString().indexOf(filter) >= 0 ||
                        item.dataPrevistoDePagamento.format('dd/MM/yyyy').toString().indexOf(filter) >= 0 ||
                        item.dataPrevistoDePagamento.format('dd-MM-yyyy').toString().indexOf(filter) >= 0 ||
                        item.pago.toString().toLowerCase().indexOf(filter) >= 0 ||
                        item.valorPago.toString().indexOf(filter) >= 0 ||
                        item.valorPagoAmortizacao.toString().indexOf(filter) >= 0 ||
                        item.valorDeJuros.toString().indexOf(filter) >= 0 ||
                        item.totalEmDivida.toString().indexOf(filter) >= 0 ||
                        item.credito.cliente.nome.toLowerCase().indexOf(filter) >= 0 ||
                        item.diasDeMora.toString().indexOf(filter) >= 0 ||
                        item.valorDaPrestacao.toString().indexOf(filter) >= 0 ||
                        item.descricao.toLowerCase().indexOf(filter) >= 0) {
                    pagamentos.add(item)
                }
                System.println(item.dateCreated)
            }
        }
    }


}
