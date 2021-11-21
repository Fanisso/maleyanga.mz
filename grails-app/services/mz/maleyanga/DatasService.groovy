package mz.maleyanga

/**
 * DatasService
 * A service class encapsulates the core business logic of a Grails application
 */

class DatasService {

    Calendar c = Calendar.getInstance()

    boolean verificarData() {
        def agora = new Date()
        Calendar c = Calendar.getInstance()
        c.setTime(agora)
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == 7 || dayOfWeek == 1) {

            return false
        }
        return true
    }



}
