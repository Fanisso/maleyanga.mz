package mz.maleyanga.contrato

import org.grails.datastore.mapping.query.Query
import org.zkoss.zul.Cell
import org.zkoss.zul.Grid
import org.zkoss.zul.Label
import org.zkoss.zul.Row

import javax.swing.text.Style

/**
 * Bloco
 * A domain class describes the data object and it's mapping to the database
 */
class Bloco {
    Grid grid
    List<Row> rows

    static belongsTo = [Contrato]
    static mapping = {
    }

    static constraints = {
    }

    /*
     * Methods of the Domain Class
     */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}
}
