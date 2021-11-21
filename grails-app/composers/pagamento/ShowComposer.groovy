package pagamento

import org.zkoss.zul.Label
import org.zkoss.zul.Textbox
import org.zkoss.zk.ui.select.annotation.Listen


class ShowComposer extends zk.grails.Composer {

Textbox tb_nome
    Label lb_nome
    def afterCompose = { window ->
        // initialize components here

    }

    @Listen("onChanging = textbox#tb_nome")
    void showBye() {
        lb_nome.value=tb_nome.value
    }
}
