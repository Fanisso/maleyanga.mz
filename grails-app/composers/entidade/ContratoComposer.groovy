package entidade


import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Button
import org.zkoss.zul.Cell
import org.zkoss.zul.Grid
import org.zkoss.zul.Label
import org.zkoss.zul.Row
import org.zkoss.zul.Rows



class ContratoComposer extends zk.grails.Composer {
    Grid gd_show
     Rows rw_show


    @Listen("onClick = button#bt_add_row")
    def addRow(){
        Label label = new Label("Nome: ")
        Label label1 = new Label(" Claudino ")
        Label label3 = new Label("Nhantumbo")
        Row row = new Row()
        Cell cell = new Cell()
        cell.colspan = 2
        cell.style="ext-align:left"
        cell.setParent(row)
        label.setParent(cell)
        label1.setParent(cell)
        label3.setParent(cell)
        row.setParent(rw_show)

    }

}
