package relatorios

import mz.maleyanga.Diarios
import mz.maleyanga.SessionStorageService
import mz.maleyanga.diario.Diario
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.saidas.Saida
import mz.maleyanga.security.Utilizador
import org.zkoss.bind.annotation.Init


class PrintDiariosDoCaixaViewModel {
    private  Date dataInicial
    private  Date dataFinal
    Utilizador selectedCaixa
    List<Diarios> diaris
    SessionStorageService sessionStorageService
    BigDecimal totalParcial =0.0
    BigDecimal totalPago=0.0
    BigDecimal totalAtivos=0.0
    BigDecimal totalPassivos=0.0

    BigDecimal getTotalParcial() {
        return totalParcial
    }

    BigDecimal getTotalAtivos() {
        return totalAtivos
    }

    BigDecimal getTotalPassivos() {
        return totalPassivos
    }

    List<Diarios> getDiaris() {
        totalParcial =0.0
        totalPago=0.0
        totalAtivos=0.0
        totalPassivos=0.0
        if(diaris==null){
            diaris = new ArrayList<Diarios>()
        }
       def diarios = Diario.findAllByDataDeReferenciaBetween(dataInicial,dataFinal)
        for(Diario diario in diarios){
            Diarios d = new Diarios(dataDeReferencia: diario.dataDeReferencia,
            dateCreated: diario.dateCreated,lastUpdated: diario.lastUpdated,
            numeroDoDiario: diario.numeroDoDiario,estado: diario.estado)
            diaris.add(d)
            def parcelas = Parcela.findAllByDiario(diario)
                parcelas.sort{it.dateCreated}
            for(Parcela p in parcelas){
                totalParcial+=p.valorParcial
                totalPago+=p.valorPago
                Diarios dp = new Diarios(parcela: p,dateCreated: p.dateCreated,dataDeReferencia: p.dataDePagamento,lastUpdated: p.lastUpdated,descricao: p.descricao,valorPago: p.valorPago)
                diaris.add(dp)
            }
            def saidas = Saida.findAllByDiario(diario)
            saidas.sort{it.dateCreated}
            for(Saida saida in saidas){
                if(saida.destino.ativo){
                    Diarios ds = new Diarios(saidaAtiva: saida,dataDeReferencia: saida.dataDePagamento,dateCreated: saida.dateCreated,descricao: saida.descricao)
                    diaris.add(ds)
                    totalAtivos+=saida.valor
                }else {

                    Diarios ds = new Diarios(saidaPassiva: saida,dataDeReferencia: saida.dataDePagamento,dateCreated: saida.dateCreated,descricao: saida.descricao)
                    diaris.add(ds)
                    totalPassivos+=saida.valor
                }
            }
        }
        return diaris
    }



    Utilizador getSelectedCaixa() {
        return selectedCaixa
    }

    void setSelectedCaixa(Utilizador selectedCaixa) {
        this.selectedCaixa = selectedCaixa
    }

    BigDecimal getTotalValorDaPrestacao() {
        return totalValorDaPrestacao
    }

    BigDecimal getTotalPago() {
        return totalPago
    }

    BigDecimal getTotalDivida() {
        return totalDivida
    }

    BigDecimal getTotalMoras() {
        return totalMoras
    }

    def getDataInicial() {

        return dataInicial
    }

    def getDataFinal() {

        return dataFinal
    }

    @Init init() {
        if(sessionStorageService.selectedCaixa!=null){
            selectedCaixa = sessionStorageService.selectedCaixa as Utilizador
        }
        if(sessionStorageService.dataInicial!=null){
            dataInicial = sessionStorageService.getDataInicial() as Date
        }
        if(sessionStorageService.dataFinal!=null){
            dataFinal = sessionStorageService.getDataFinal() as Date
        }


    }



}
