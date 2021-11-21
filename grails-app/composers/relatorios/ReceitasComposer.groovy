package relatorios

import org.zkoss.zul.Button
import org.zkoss.zul.Datebox


class ReceitasComposer extends zk.grails.Composer implements Serializable {
    private static final long serialVersionUID = 1

    Datebox inicio
    Datebox fim
    Button btn

    def afterCompose = { window ->

    }



}
