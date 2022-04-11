package pedidoDeCredito

import grails.plugin.springsecurity.SpringSecurityService
import mz.maleyanga.ClienteService
import mz.maleyanga.CreditoService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.pedidoDeCredito.PedidoDeCredito
import mz.maleyanga.pedidoDeCredito.Penhora
import mz.maleyanga.security.Utilizador
import mz.maleyanga.settings.DefinicaoDeCredito
import grails.transaction.Transactional
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList

@Transactional
@Service
class PedidoDeCreditoViewModel {
  SpringSecurityService springSecurityService
  private ListModelList<Cliente> clientes
  private ListModelList<PedidoDeCredito> pedidos
  private ListModelList<Penhora> penhoras
private PedidoDeCredito sPDC
  private String filter
  private String filterCliente
  private  Utilizador utilizador
  CreditoService creditoService
  ClienteService clienteService
  private ListModelList<DefinicaoDeCredito> definicoes
  BigDecimal valor
  Cliente selectedCliente

  Cliente getSelectedCliente() {
    return selectedCliente
  }

  @NotifyChange(["penhoras"])
  void setSelectedCliente(Cliente selectedCliente) {
    this.selectedCliente = selectedCliente
  }

  @Command
  void doSearchCliente() {
    clientes.clear()
    clientes.clear()
    List<Cliente> allItems = clienteService.findAllByName(filterCliente)
    if (filterCliente != null &&! "".equals(filterCliente))
    {
      for (Cliente item : allItems) {
        if (item.nome.toLowerCase().indexOf(filterCliente.toLowerCase()) >= 0 ||

                item.numeroDeIndentificao.indexOf(filterCliente) >= 0) {
          clientes.add(item)

        }
      }
    }
  }
  String getFilterCliente() {
    return filterCliente
  }

  void setFilterCliente(String filterCliente) {
    this.filterCliente = filterCliente
  }

  ListModelList<DefinicaoDeCredito> getDefinicoes() {

    if(definicoes==null){
      definicoes = new ListModelList<>(DefinicaoDeCredito.all)
    }

    return definicoes
  }
  String getFilter() {
    return filter
  }

  void setFilter(String filter) {
    this.filter = filter
  }

  BigDecimal getValor() {
    return somar()
  }
  @NotifyChange(["pagamentos","info"])
  @Command
  void doSearch() {
    pedidos.clear()
    List<PedidoDeCredito> allItems = PedidoDeCredito.all
    if (filter == null || "".equals(filter)) {
      pedidos.addAll(allItems)
    } else {
      for (PedidoDeCredito item : allItems) {
        if (item.cliente.nome.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                item.motivo.toString().indexOf(filter) >= 0 ||
                item.valorDeCredito.toString().indexOf(filter) >= 0 ||
                item.dateCreated.format('dd/MM/yyyy').toString().indexOf(filter) >= 0 ||
                item.estado.indexOf(filter) >= 0) {
          pedidos.add(item)
        }
      }
    }
  }

  ListModelList<PedidoDeCredito> getPedidos() {
    if(pedidos==null){
      pedidos = new ListModelList<PedidoDeCredito>(PedidoDeCredito.findAllByEstadoOrEstado("aberto","aprovado"))
    }
    return pedidos
  }

  ListModelList<Penhora> getPenhoras() {
    if(penhoras==null){
      penhoras = new ListModelList<Penhora>()
    }
    return penhoras
  }

  PedidoDeCredito getsPDC() {
    return sPDC
  }

  @NotifyChange(["penhoras"])
  void setsPDC(PedidoDeCredito sPDC) {
    this.sPDC = sPDC
  }

  ListModelList<Cliente> getClientes() {
    if(clientes==null){
      clientes = new ListModelList<Cliente>()
    }
    return clientes
  }
  @Wire Label info

    @Init init() {
        // initialzation code here
    }

  @Command
  @NotifyChange(['penhoras','sPDC'])
  def addPenhora(){
    info.value = ""
    penhoras.add(new Penhora())

  }
  @Command
  @NotifyChange(["pedidos","sPDC"])
  def showPenhoras(){
    info.value = ""

  }
  @Command
  @NotifyChange(['penhoras','sPDC'])
  def addPDC(){
    info.value = ""
    sPDC = new PedidoDeCredito()

    penhoras.add(new Penhora())
  }
  @Command
  @NotifyChange(['penhoras','sPDC'])
  def addCredito(){
    if(sPDC.creditado){
      creditoService.pedidoDeCredito = sPDC
      Executions.sendRedirect("/credito/")
    }else {
      info.value="Este Pedido não foi aprovado!"
      info.style = "color:red;font-weight;font-size:14ptpt;background:back"
    }

  }
  @Command
  @NotifyChange(["penhoras"])
  def limpar(){
    info.value = ""
    penhoras.clear()
  }
  @Command
  @NotifyChange(["sPDC"])
  def aprovarPedido(){
    info.value=""
    sPDC.creditado = !sPDC.creditado
    if(sPDC.creditado){
      sPDC.estado="aprovado"
    }else {
      sPDC.estado="reprovado"
    }
  }
  @Command
  @NotifyChange(["sPDC","pedidos"])
  def updatePedido(){
    Utilizador user = springSecurityService.currentUser as Utilizador
    if (!user.authorities.any { it.authority == "PEDIDO_DE_CREDITO_UPDATE" }) {
      info.value="Este utilizador não tem permissão para executar esta acção !"
      info.style = "color:red;font-weight;font-size:14ptpt;background:back"
      return
    }
    if(sPDC.creditado){
      sPDC.dataDeAprovacao= new Date()
      }
    sPDC.merge(flush: true)
    info.value = "Pedido de crédito actualizado com sucesso!"
    info.style = "color:blue;font-weight;font-size:14ptpt;background:back"
  }
  @Command
  @NotifyChange(['penhoras','sPDC','pedidos','penhoras'])
  def salvarPDC(){
    info.value = ""
    try {
      if(sPDC.id!=null){
        info.value = "Este Pedido de crédito já foi gravado na base de dados!"
        info.style = "color:blue;font-weight;font-size:14ptpt;background:back"
        return
      }
      if(penhoras.empty){
        info.value = "O pedido deve ter pelo menos uma penhora!"
        info.style = "color:blue;font-weight;font-size:14ptpt;background:back"
        return
      }
      if(somar()<sPDC.valorDeCredito){

        info.value = "A soma dos valores das penhoras deve ser superior ao valor pedido!"
        info.style = "color:blue;font-weight;font-size:14ptpt;background:back"
        return
      }
      utilizador = Utilizador.findById(springSecurityService.principal?.id)
      sPDC.utilizador = utilizador

      sPDC.estado = "aberto"
      System.println(penhoras)
      sPDC.creditado = false
      sPDC.valorDaPenhora = somar()
      sPDC.cliente = selectedCliente
      sPDC.save(flush: true)
      def penhoraDB = PedidoDeCredito.findById(sPDC.id)
      System.println(penhoraDB.id)
      for(Penhora penhora in penhoras){
        penhora.pedidoDeCredito = penhoraDB
        penhora.cliente = selectedCliente
        penhora.save(flush: true)

      }




      if(penhoraDB!=null){
        pedidos.add(penhoraDB)
        info.value = "O Pedido de crédito foi criado com sucesso!"
        info.style = "color:blue;font-weight;font-size:14ptpt;background:back"

      }

    }catch(Exception e){
      System.println(e.toString())
    }

  sPDC= new PedidoDeCredito()

  }

  @Command
  @NotifyChange(['valor'])
  BigDecimal somar(){
    BigDecimal valor= 0.0
    penhoras.each {
      valor+=it.valor
    }
    return valor
  }
}
