package mz.maleyanga.security

class Role implements Serializable{
    private static final long serialVersionUID = 1
    String authority

    static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
    }

      String toString() {
          return "${authority}"
    }
}
