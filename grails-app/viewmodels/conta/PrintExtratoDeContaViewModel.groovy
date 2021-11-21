package conta

import mz.maleyanga.ExtratoDeConta
import mz.maleyanga.SessionStorageService
import mz.maleyanga.conta.Conta
import mz.maleyanga.transacao.Transacao
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList

class PrintExtratoDeContaViewModel {
    Date dataInicial
    Date dataFinal
    Set<ExtratoDeConta> extratoHash = new HashSet<ExtratoDeConta>()
    Conta conta
    SessionStorageService sessionStorageService
    ListModelList<Transacao> transacoes = new ListModelList<Transacao>()
    ListModelList<ExtratoDeConta>  extratoDeContas = new ListModelList<ExtratoDeConta>()
    BigDecimal totalCreditos = 0.0
    BigDecimal totalDebitos = 0.0
    BigDecimal saldoAnterior = 0.0

    BigDecimal getSaldoAnterior() {
        BigDecimal saldo = 0.0
        getExtratoDeContas()
        somar()
        if(!extratoDeContas.empty){
            saldo  = extratoDeContas.first().saldo
            saldo-=extratoDeContas.first().debito
            saldo+=extratoDeContas.first().credito
        }

        return saldo
    }
    def somar(){
        totalDebitos = 0.0
        totalCreditos = 0.0
        for(ExtratoDeConta e in extratoDeContas){
            if(e.credito>0.0){
                totalCreditos+=e.credito
            }
            if(e.debito>0.0){
                totalCreditos+=e.debito
            }
        }
    }
    BigDecimal getTotalCreditos() {
        return totalCreditos
    }


    BigDecimal getTotalDebitos() {
        return totalDebitos
    }

    ListModelList<ExtratoDeConta> getExtratoDeContas() {
        getTransacoes()
        BigDecimal saldo=0.0
        List extratos = new ListModelList<ExtratoDeConta>()

        for(Transacao transacao in transacoes){

            if(transacao.credito){

                ExtratoDeConta extratoDeConta = new ExtratoDeConta()
                saldo-=transacao.valor
                extratoDeConta.data = transacao.dateCreated
                extratoDeConta.credito=transacao.valor
                extratoDeConta.debito = 0.0
                extratoDeConta.descricao = transacao.descricao
                extratoDeConta.saldo=saldo
                extratoDeContas.add(extratoDeConta)


            }else {

                ExtratoDeConta extratoDeConta = new ExtratoDeConta()
                saldo+=transacao.valor
                extratoDeConta.data = transacao.dateCreated
                extratoDeConta.credito=0.0
                extratoDeConta.debito =transacao.valor
                extratoDeConta.descricao = transacao.descricao
                extratoDeConta.saldo =saldo
                extratoDeContas.add(extratoDeConta)

            }

        }
        for(ExtratoDeConta e in extratoDeContas){
            if (e.data.after(dataInicial)&&e.data.before(dataFinal)){
                extratos.add(e)
              }
        }

        return extratos
    }

    ListModelList<Transacao> getTransacoes() {
        if(conta.transacoes!=null){
            for(Transacao transacao in conta.transacoes){
                transacoes.add(transacao)
            }
        }

        transacoes.sort{it.id}
        return transacoes
    }

    @Init init() {
        Conta contaSession = sessionStorageService.getConta() as Conta
        if(sessionStorageService.dataInicial!=null){
            dataInicial = sessionStorageService.getDataInicial() as Date
        }
        if(sessionStorageService.dataFinal!=null){
            dataFinal = sessionStorageService.getDataFinal() as Date
        }
        if(contaSession!=null){
            conta = Conta.findById(contaSession.id)
            conta.calcularSaldo()
        }

    }

    Conta getConta() {
        return conta
    }
}
