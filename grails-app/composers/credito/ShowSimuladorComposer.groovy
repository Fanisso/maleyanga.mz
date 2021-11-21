package credito

import mz.maleyanga.PagamentoService
import mz.maleyanga.SimuladorService
import mz.maleyanga.simulador.Item
import mz.maleyanga.simulador.Simulador
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.*

class ShowSimuladorComposer extends zk.grails.Composer implements Serializable {
    private static final long serialVersionUID = 1
    SimuladorService simuladorService
    PagamentoService pagamentoService
    @Wire
    Listbox lb_items = new Listbox()

    def afterCompose = { window ->
        renderLb()
    }

    def renderLb () {

        itemsList()
        lb_items.getItems() .clear()
        lb_items.setItemRenderer(new ListitemRenderer<Item>() {


            @Override
            void render(Listitem item, Item it, int i) throws Exception {
                new Listcell(it.meses).setParent(item)
                new Listcell(it.saldoDevedor).setParent(item)
                new Listcell(it.prestacoes).setParent(item)
                new Listcell(it.amortizacao).setParent(item)
                new Listcell(it.juros).setParent(item)
                item.setValue(it)


            }
        })
    }

    def itemsList(){
        String meses
        String saldoDevedor
        String prestacoes
        String amortizacao
        String juros
        def its = new ArrayList<Item>()
      //  simuladorService.gerarExtrato()
      //  its=simuladorService.its

        Simulador simulador = simuladorService.simulador
        def rate = simulador.rate/100
         prestacoes = -pagamentoService.pmt(rate,simulador.nper,simulador.pv,0,0)
        prestacoes = prestacoes.setScale(2, BigDecimal.ROUND_HALF_EVEN)
        System.println(prestacoes)
        Item itm0 = new Item()

        itm0.saldoDevedor=simulador.pv.setScale(2, BigDecimal.ROUND_HALF_EVEN)

        itm0.meses="0"
        its.add(itm0)


        for(int x=1;x<=simulador.nper;x++) {
            def saldoDevedorLast = its.last().saldoDevedor as BigDecimal
            saldoDevedorLast = saldoDevedorLast.setScale(2, BigDecimal.ROUND_HALF_EVEN)
            juros = saldoDevedorLast*rate
            juros=juros.setScale(2, BigDecimal.ROUND_HALF_EVEN)
             amortizacao = prestacoes-juros
             saldoDevedor = saldoDevedorLast-amortizacao

        def d = new Item(meses: x.toString(),saldoDevedor: saldoDevedor,prestacoes: prestacoes,amortizacao: amortizacao,juros:juros)
            its.add(d)

        }
        ListModelList lml = new ListModelList(its,true)
        lb_items.setModel(lml)
    }
}
