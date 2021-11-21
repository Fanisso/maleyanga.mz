package credito

import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList

class ListCreditosViewModel {
    private ListModelList<Credito> creditos
    Credito selectedCredito
    Cliente selectedCliente


    Credito getSelectedCredito() {
        return selectedCredito
    }

    void setSelectedCredito(Credito selectedCredito) {
        this.selectedCredito = selectedCredito
    }

    ListModelList<Credito> getCreditos() {
        if(creditos==null){
            creditos = new ListModelList<Credito>()
        }

        return creditos
    }

    Cliente getSelectedCliente() {
        return selectedCliente
    }


    void setSelectedCliente(Cliente selectedCliente) {
        this.selectedCliente = selectedCliente
    }
}
