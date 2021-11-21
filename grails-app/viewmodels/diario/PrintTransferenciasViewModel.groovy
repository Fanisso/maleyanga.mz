package diario

import mz.maleyanga.ComposerService
import mz.maleyanga.diario.Diario
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.saidas.Saida
import org.springframework.stereotype.Service
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
@Service
class PrintTransferenciasViewModel {
    Diario diario
    BigDecimal totalSaidas = 0.0
    ComposerService composerService
    ListModelList<Saida> saidas
    Diario getDiario() {

        return diario
    }
    BigDecimal getTotalSaidas() {
        totalSaidas = 0.0
        def saidas = Saida.findAllByDiario(diario)
        for(Saida saida in saidas){
            totalSaidas +=saida.valor
        }
        return totalSaidas
    }
    @Init init() {
        diario = Diario.findById(composerService.diario.id)
    }

    ListModelList<Saida> getSaidas() {
        if(saidas ==null){
            saidas = new ListModelList<Saida>(Saida.findAllByDiario(diario))
        }
        return saidas
    }
}
