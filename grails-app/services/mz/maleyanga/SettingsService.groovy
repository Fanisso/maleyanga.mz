package mz.maleyanga

import mz.maleyanga.settings.Settings

/**
 * SettingsService
 * A service class encapsulates the core business logic of a Grails application
 */

class SettingsService {
    Settings settings

    def salvar(Settings settings) {

        settings.merge(flush: true)
    }

    Settings getSettings() {
        return Settings.findByNome("settings")
    }

}
