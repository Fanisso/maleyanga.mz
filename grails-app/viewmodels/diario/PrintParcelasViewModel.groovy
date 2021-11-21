package diario

import mz.maleyanga.ComposerService
import mz.maleyanga.diario.Diario
import mz.maleyanga.pagamento.Parcela
import org.springframework.security.access.method.P
import org.springframework.stereotype.Service
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
@Service
class PrintParcelasViewModel {
    ComposerService composerService
    Diario diario
    BigDecimal totalParcelas = 0.0
    List<Parcela> parcelas

    BigDecimal getTotalParcelas() {
        totalParcelas = 0.0
        def parcelas = Parcela.findAllByDiario(diario)
       for(Parcela parcela in parcelas){
           totalParcelas +=parcela.valorParcial
       }
        return totalParcelas
    }

    List<Parcela> getParcelas() {
        if(parcelas==null){
            parcelas = new ListModelList<Parcela>(Parcela.findAllByDiario(diario))
        }
           return parcelas
    }

    Diario getDiario() {

        return diario
    }

    void setDiario(Diario diario) {
        this.diario = diario
    }

    @Init init() {
    diario = Diario.findById(composerService.diario.id)

    }



}
