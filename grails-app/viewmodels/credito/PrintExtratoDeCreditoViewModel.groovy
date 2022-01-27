package credito


import mz.maleyanga.ExtratoDeCredito
import mz.maleyanga.ExtratoDePagamento
import mz.maleyanga.SessionStorageService
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.pagamento.Remissao
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
import java.math.RoundingMode

class PrintExtratoDeCreditoViewModel {
    SessionStorageService sessionStorageService
    Credito creditoInstance
    String message
    BigDecimal totalDebito= 0
    BigDecimal totalCredito = 0
    BigDecimal totalJurosDeMora = 0
    BigDecimal totalJuros = 0
    BigDecimal totalPrestacao = 0
    BigDecimal totalValorPagoNoPrazo = 0
    BigDecimal totalIncidencia = 0
    BigDecimal totalMoras = 0
    BigDecimal totalRemissao = 0
    BigDecimal totalDivida = 0
    BigDecimal valorDaPrestacao = 0
    BigDecimal saldoEmMora = 0
    Date data
    Integer numeroDePrestacoes
    Integer numeroDePrestacoesEmDia = 0
    Boolean dividaSemMoras
       // ((vm.valorDaPrestacao*vm.numeroDePrestacoesEmDia)+vm.totalJurosDeMora)+vm.totalCredito
    Date getData() {
        return new Date()
    }

    BigDecimal getSaldoEmMora() {
        saldoEmMora = 0
        saldoEmMora = valorDaPrestacao*numeroDePrestacoesEmDia+totalJurosDeMora+totalCredito
        return saldoEmMora.setScale(2, RoundingMode.HALF_UP)
    }

    Integer getNumeroDePrestacoes() {
        Integer num = 0
        for(Pagamento p in creditoInstance.pagamentos){
            if (p.descricao!="CAPITALIZACAO"){
                num++
            }
        }
        return num
    }

    Integer getNumeroDePrestacoesEmDia() {
        Integer num = 0
        Date hoje = new Date()
        for(Pagamento p in creditoInstance.pagamentos){
            if(p.descricao!="CAPITALIZACAO"){
                if(p.dataPrevistoDePagamento.before(hoje)){
                    num++
                }
            }

        }
        return num

    }

    BigDecimal getValorDaPrestacao() {
        def pag = creditoInstance?.pagamentos?.find {it?.descricao=="1º- Prestação"}
        BigDecimal  valor=pag?.valorDaPrestacao
        System.println(valor)
        return   valor*(-1)
    }

    BigDecimal getTotalPrestacao() {
        return totalPrestacao
    }

    BigDecimal getTotalValorPagoNoPrazo() {
        return totalValorPagoNoPrazo
    }

    BigDecimal getTotalIncidencia() {
        return totalIncidencia
    }

    BigDecimal getTotalMoras() {
        return totalMoras
    }

    BigDecimal getTotalRemissao() {
        return totalRemissao
    }

    BigDecimal getTotalDivida() {
        return totalDivida
    }
    private ListModelList<Pagamento> pagamentos
    private ListModelList<ExtratoDeCredito> extratoDeCreditos = new ListModelList<ExtratoDeCredito>()
    private ListModelList<ExtratoDePagamento> extratoDePagamentos = new ListModelList<ExtratoDePagamento>()

    @Wire  btnHello


    @Init init() {
        creditoInstance = Credito.findById(sessionStorageService?.credito?.id)

        somar()
        somarPagamentos()
        // pagamentoService.calcularMoraCaPital(creditoInstance)

    }

    def somar(){
        extratoDeCreditos.each {
            totalDebito+=it.debito
            totalCredito+=it.credito
            totalJurosDeMora+=it.jurosDeMora
            totalJuros+=it.juros
            totalMoras+=it.valorEmMora
        }

    }
    def somarPagamentos(){
        extratoDePagamentos.each {
            totalPrestacao += it.valorDaPrestacao
            totalValorPagoNoPrazo += it.totalPagoNoPrazo
            totalIncidencia += it.totalIncidencia
            totalMoras += it.jurosDeMora
            totalRemissao += it.valorDaRemissao
            totalDivida += it.totalEmDivida
        }
    }

    @NotifyChange(['message'])
    @Command clickMe() {
        message = "Clicked"
    }

    ListModelList<ExtratoDeCredito> getExtratoDeCreditos() {

        BigDecimal juros=0.00
        for(Pagamento pagamento1 in creditoInstance.pagamentos.sort{it.id}){
            if(pagamento1.descricao!="CAPITALIZACAO"){
                juros += pagamento1.valorDaPrestacao*(-1)
            }

        }
        BigDecimal saldo = juros
        ExtratoDeCredito extrato = new ExtratoDeCredito()
        extrato.debito = saldo
        extrato.credito = 0.0
        extrato.descricao = "EMPRESTIMO C/ JUROS"
        extrato.jurosDeMora = 0.0
        extrato.juros = 0.0
        extrato.data = creditoInstance.dateConcecao
        extrato.saldo = 0.0
        extratoDeCreditos.add(extrato)


        for(Pagamento pagamento in creditoInstance.pagamentos.sort{it.id}){
            for(Parcela parcela in pagamento.parcelas.sort{it.id}){
                if(parcela.valorPago>0.0){
                    ExtratoDeCredito extratoDeCredito = new ExtratoDeCredito()
                    extratoDeCredito.data = parcela.dataDePagamento
                    extratoDeCredito.descricao = "AMORT. D. "+pagamento.descricao
                    extratoDeCredito.debito = 0.0
                    extratoDeCredito.credito = parcela.valorPago*(-1)
                    extratoDeCredito.jurosDeMora = 0.0
                    extratoDeCredito.saldo=0.0
                    extratoDeCreditos.add(extratoDeCredito)

                }

            }
            for(Remissao remissao in pagamento.remissoes.sort{it.id}){
                ExtratoDeCredito extratoDeCredito = new ExtratoDeCredito()
                extratoDeCredito.data = remissao.createdDate
                extratoDeCredito.descricao = "REMISSÃO DA DÍVIDA"
                extratoDeCredito.debito = 0.0
                extratoDeCredito.credito = remissao.valorDaRemissao*(-1)
                extratoDeCredito.jurosDeMora = 0.0
                extratoDeCredito.saldo=0.0
                extratoDeCreditos.add(extratoDeCredito)
            }
            if (pagamento.valorDeJurosDeDemora * (-1) > 0.0) {
                Calendar c = Calendar.getInstance()
                c.setTime(pagamento.dataPrevistoDePagamento)
                c.add(Calendar.DAY_OF_MONTH, 1)
                ExtratoDeCredito extratoDeCredito = new ExtratoDeCredito()
                extratoDeCredito.data = c.getTime()
                extratoDeCredito.descricao = "J. DE MORA da " + pagamento.descricao
                extratoDeCredito.debito = 0.0
                extratoDeCredito.credito = 0.0
                extratoDeCredito.diasDeMora = pagamento.diasDeMora+""
                if(creditoInstance.ignorarValorPagoNoPrazo){
                    extratoDeCredito.valorEmMora = (pagamento.valorDaPrestacao) *(-1)
                    System.println("ignorarValorPagoNoPrazo"+creditoInstance.ignorarValorPagoNoPrazo)
                }else {
                    extratoDeCredito.valorEmMora = (pagamento.valorDaPrestacao+pagamento.totalPagoNoPrazo) *(-1)
                    System.println("ignorarValorPagoNoPrazo=!"+creditoInstance.ignorarValorPagoNoPrazo)
                }
                extratoDeCredito.jurosDeMora = pagamento.valorDeJurosDeDemora * (-1)
                extratoDeCredito.saldo = 0.0
                extratoDeCreditos.add(extratoDeCredito)
            }
            if (pagamento.descricao=="CAPITALIZACAO") {
                ExtratoDeCredito extratoDeCredito = new ExtratoDeCredito()
                extratoDeCredito.data = pagamento.dataDaCriacao
                extratoDeCredito.descricao =pagamento.descricao
                extratoDeCredito.debito = 0.0
                extratoDeCredito.credito = 0.0
                extratoDeCredito.jurosDeMora = pagamento.valorDaPrestacao * (-1)
                extratoDeCredito.valorEmMora = 0.0
                extratoDeCredito.saldo = 0.0
                extratoDeCreditos.add(extratoDeCredito)
            }

        }
        extratoDeCreditos.sort{it.data}
        BigDecimal sal = 0.0

        for(ExtratoDeCredito extratoDeCredito in extratoDeCreditos){
            sal+=extratoDeCredito.credito
            sal+=extratoDeCredito.debito
            sal+=extratoDeCredito.jurosDeMora
            extratoDeCredito.saldo=sal

        }

        BigDecimal salm = 0.0

        for(ExtratoDeCredito extratoDeCredito in extratoDeCreditos){
            if(extratoDeCredito.jurosDeMora>0){
                salm+=extratoDeCredito.jurosDeMora
                salm+=extratoDeCredito.valorEmMora


            }

            salm+=extratoDeCredito.credito

            if(salm<0){
                salm = saldoEmMora
            }

            extratoDeCredito.saldoMora=salm
        }
        for( int x=1;x<extratoDeCreditos.size();x++){
            if(extratoDeCreditos[x].descricao=="CAPITALIZACAO"){
                extratoDeCreditos[x].valorEmMora = extratoDeCreditos[x-1].saldo
            }
        }
        /*  if (extratoDeCreditos.last().saldoMora>extratoDeCreditos.last().saldo){
              extratoDeCreditos.last().saldoMora = extratoDeCreditos.last().saldo
          }*/
        Date date = new Date()
        Calendar c = Calendar.getInstance()
        c.setTime(date)
        c.add(Calendar.DAY_OF_MONTH,-1)
        System.println(creditoInstance.pagamentos.last().dataPrevistoDePagamento)
        /*  if(c.getTime().after(creditoInstance.pagamentos.last().dataPrevistoDePagamento)){
              if(saldoEmMora>extratoDeCreditos.last().saldo){
                  saldoEmMora=extratoDeCreditos.last().saldo
              }
          }*/
        somar()
        return extratoDeCreditos
    }


    ListModelList<Pagamento> getPagamentos() {
        if(pagamentos==null){
            pagamentos = new LinkedHashSet<Pagamento>(Pagamento.findAllByCredito(creditoInstance))
            pagamentos.sort{it.id}
        }
        somar()
        return pagamentos
    }

    BigDecimal getTotalDebito() {
        return totalDebito
    }

    BigDecimal getTotalCredito() {
        return totalCredito
    }

    BigDecimal getTotalJurosDeMora() {
        return totalJurosDeMora
    }

    BigDecimal getTotalJuros() {
        return totalJuros
    }

}
