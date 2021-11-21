package mz.maleyanga

class CalcularIdade {
    static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first)
        Calendar b = getCalendar(last)
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR)
        if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
            diff--
        }
        return diff
    }

    static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        return cal
    }
}
