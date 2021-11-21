package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.conta.Conta
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.transacao.Transacao

import java.sql.SQLDataException

/**
 * ContaService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class ContaService {
    Date dataInicial
    Date dataFinal
    Conta conta
    Credito credito

    def update(Conta conta1) {
        try {
            Conta.merge(conta1)
            return true
        } catch (Exception e) {
            System.println(e.toString())
            return false
        }
    }

    boolean debitoAutomatico(Pagamento pagamento) {
        try {
            def designacaoDaConta = 'conta_cliente_' + pagamento.credito.cliente.id
            def contaDb = Conta.findByDesignacaoDaConta(designacaoDaConta)
            def valorEmDivida = pagamento.totalEmDivida * (-1)
            if (contaDb != null) {
                System.println('debitoAutomatico.contaDb.saldo=' + contaDb.saldo)
                System.println('debitoAutomatico.contaDb.credito=' + contaDb.credito)
                System.println('debitoAutomatico.contaDb.debito=' + contaDb.debito)
                System.println('debitoAutomatico.valorEmDivida='+valorEmDivida)
    System.print('contaDb.saldo>=valorEmDivida')
    System.print(contaDb.saldo>=valorEmDivida)
    System.print(contaDb.saldo+'>'+valorEmDivida)

                if(contaDb.saldo>=valorEmDivida){
                    System.println(contaDb.saldo+'>'+valorEmDivida)
                    Transacao transacao = new Transacao(valor:valorEmDivida, descricao: 'debito automático ref. ao Pag. Nº'+pagamento.id, credito: false)
                    if (contaDb?.transacoes == null) {
                        contaDb.transacoes = new ArrayList<Transacao>()
                    }
                    contaDb.transacoes.add(transacao)
                    contaDb.save(flush: true)
                    return true
                }

            }

        }catch (Exception e){
            System.println('contaService.debitoAutomatico'+e.toString())
            return false
        }
    }

    boolean creditoAutomatico(Pagamento pagamento, BigDecimal valor){
        try {
            def designacaoDaConta = 'conta_cliente_'+pagamento.credito.cliente.id
            def contaDb = Conta.findByDesignacaoDaConta(designacaoDaConta)
            if (contaDb != null) {
                Transacao transacao = new Transacao(valor: valor, descricao: 'crédito automático ref. ao Pag. º'+pagamento.id, credito: true)
                if (contaDb?.transacoes == null) {
                    contaDb.transacoes = new ArrayList<Transacao>()
                }
                contaDb.transacoes.add(transacao)
                contaDb.save(flush: true)
                return true
            }

        }catch (Exception e){
            System.println('contaService.debitoAutomatico'+e.toString())
            return false
        }
    }


  boolean alocacaoDeVaoresNasContas(Pagamento pagamento){
      try {
          def pagamentoDb = Pagamento.findById(pagamento.id)
          if(!pagamentoDb.pago||pagamentoDb==null||pagamentoDb.valoresAlocados){
              return false
          }

               def conta_juros = Conta.findByDesignacaoDaConta('conta_juros')
               def conta_moras = Conta.findByDesignacaoDaConta('conta_juros_de_mora')
               def conta_amortizacao = Conta.findByDesignacaoDaConta('conta_amortizacao')
                if(pagamentoDb.valorPagoJuros>0){
                    Transacao transacao_j = new Transacao(descricao: 'alocacao do v. de juros ref. ao Pag. Nº.'+pagamentoDb.id,valor: pagamentoDb.valorPagoJuros,credito: true)
                    if(conta_juros!=null){
                        if(conta_juros.transacoes==null){
                            conta_juros.transacoes = new ArrayList<Transacao>()
                        }
                        conta_juros.transacoes.add(transacao_j)
                        conta_juros.save(flush: true)
                    }else System.println('alocacaoDeVaoresNasContas.conta_juros==null')

                }
                if(pagamentoDb.valorPagoDemora>0){
                    Transacao transacao_moras = new Transacao(descricao: 'alocacao do valor de mora ref. ao Pag. Nº.'+pagamentoDb.id,valor: pagamentoDb.valorPagoDemora,credito: true)
                   if(conta_moras!=null){
                       if(conta_moras.transacoes==null){
                           conta_moras.transacoes = new ArrayList<Transacao>()
                       }
                       conta_moras.transacoes.add(transacao_moras)
                       conta_moras.save(flush: true)
                   }else System.println('alocacaoDeVaoresNasContas.conta_moras==null')

                }
                if(pagamentoDb.valorPagoAmortizacao>0){
                    Transacao transacao_amortizacao = new Transacao(descricao: 'alocacao do v. de amortização ref. ao Pag. Nº.'+pagamentoDb.id,valor: pagamentoDb.valorPagoAmortizacao,credito: true)
                    if(conta_amortizacao!=null){
                        if(conta_amortizacao.transacoes==null){
                            conta_amortizacao.transacoes = new ArrayList<>()
                        }
                        conta_amortizacao.transacoes.add(transacao_amortizacao)
                        conta_amortizacao.save(flush: true)
                    }else System.println('alocacaoDeVaoresNasContas.conta_amortização==null')


                }
          pagamentoDb.valoresAlocados=true
          pagamentoDb.save(flush: true)
          return true

      }catch (Exception e){
          System.println('ContaService.alocacaoDeVaoresNasContas'+e.toString())
          return false
      }
  }

    boolean debidoAutomatico(Credito credito) {
        try {
            def conta = Conta.findByDesignacaoDaConta('conta_capital')
            if (conta != null) {
                Transacao transacao = new Transacao(valor: credito.valorCreditado, descricao: 'debito automático ref. ao Credito. Nº' + credito.id, credito: false)
                if (conta?.transacoes == null) {
                    conta.transacoes = new ArrayList<Transacao>()
                }
                conta.transacoes.add(transacao)
                conta.save(flush: true)
                return true
            }
        } catch (Exception e) {
            System.println(e.toString())
            return false
        }
    }

    boolean creditoAutomatico(BigDecimal valor, Long id) {
        try {
            def conta = Conta.findByDesignacaoDaConta('conta_capital')
            if (conta != null) {
                Transacao transacao = new Transacao(valor: valor, descricao: 'crédito automático ref. ao Credito. Nº' + id, credito: true)
                if (conta?.transacoes == null) {
                    conta.transacoes = new ArrayList<Transacao>()
                }
                conta.transacoes.add(transacao)
                conta.save(flush: true)
                return true
            } else {
                System.println("A conta_capital não foi achado!")
            }
        } catch (Exception e) {
            System.println(e.toString())
            return false
        }
    }

    def save(Conta conta1) {
        try {
            conta1.save()
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }

    }

    def merge(Conta conta1) {
        try {
            conta1.merge(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }

    }

    def getContaManeio() {

    }

    List<Conta> findAllContaCaixa() {
        def contas = Conta.findAllByFinalidade("conta_caixa")
        return contas
    }
}
