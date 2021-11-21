package mz.maleyanga

class ControllerRedirect {
    private ArrayList<CurrentAction> actions
    private boolean isOnFirstAction// � true quando a action corrente e a inicial, isto e, quando a lista de actions for empty

    ControllerRedirect() {
        isOnFirstAction = true
        actions = new ArrayList<CurrentAction>()
    }

    void addAction(CurrentAction action) {
        isOnFirstAction = false
        actions.add(action)
    }

    CurrentAction getCurrentAction() {
        if (actions.isEmpty()) return null

        CurrentAction voAux = actions.get(actions.size() - 1)
        actions.remove(actions.size() - 1)

        if (actions.isEmpty()) isOnFirstAction = true
        return voAux
    }

    boolean hasActionToGo() {
        if (actions == null) return false

        return !actions.isEmpty()
    }

    boolean getIsOnFirstAction() {
        return isOnFirstAction
    }
}
