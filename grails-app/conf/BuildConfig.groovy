grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.server.port.http = 8090
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 2048, minMemory: 1024, debug: false, maxPerm: 1024, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 2048, minMemory: 1024, debug: false, maxPerm: 1024, forkReserve:false],
    //war: [maxMemory: 1280, minMemory: 128, debug: false, maxPerm: 320, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 2048, minMemory: 128, debug: false, maxPerm: 1024]
]
grails.tomcat.jvmArgs= ["-Xms256m",  "-Xmx1024m", "-XX:PermSize=512m", "-XX:MaxPermSize=512m"]
grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        mavenRepo "http://dl.bintray.com/zkgrails/release"
        mavenRepo "http://www.it-jw.com/maven"
        mavenRepo "http://repo.grails.org/grails/core"
        mavenRepo "http://repo.desirableobjects.co.uk/"

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
        // runtime 'mysql:mysql-connector-java:5.1.27'
         runtime 'org.postgresql:postgresql:9.3-1100-jdbc41'
        test "org.grails:grails-datastore-test-support:1.0-grails-2.3"
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.54"
       // compile "org.grails.plugins:glassfish:0.1.2"
        // plugins for the compile step
       // compile ":scaffolding:2.0.3"
        compile ':cache:1.1.7'

        // plugins needed at runtime but not for compilation
        runtime ":hibernate:3.6.10.16" // or ":hibernate4:4.3.5.4"
        //runtime ":database-migration:1.4.0"
       // runtime ":jquery:1.11.1"
        runtime ":resources:1.2.8"
       // runtime ":rest:0.7"

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0.1"
        //runtime ":cached-resources:1.1"
        //runtime ":yui-minify-resources:0.1.5"

        // An alternative to the default resources plugin is the asset-pipeline plugin
        //compile ":asset-pipeline:1.6.1"

        // Uncomment these to enable additional asset-pipeline capabilities
        //compile ":sass-asset-pipeline:1.5.5"
        //compile ":less-asset-pipeline:1.5.3"
        //compile ":coffee-asset-pipeline:1.5.0"
        //compile ":handlebars-asset-pipeline:1.3.0.1"

        /*Add by developer*/

        compile ':spring-security-core:2.0-RC3'
       // compile ":birt-report:4.3.0.3"
        compile ':zk:2.5.2'
        compile ":zk-angular:1.0.0.M3"   // ZK Angular support
       // compile ":zk-atlantic:1.0.1" // ZK Atlantic
        compile "org.grails.plugins:quartz:1.0.2"
        compile ":joda-time:1.5"
        compile ":kickstart-with-bootstrap:1.1.0"
        compile ":calendar:1.2.1"
       // compile ':attachmentable:0.3.0'
       // compile ":angularjs-resources:1.4.2"
        compile ":glyph-icons:0.1.0"
      //  compile ":searchable:0.6.9"
        compile ":webxml:1.4.1"
      //  compile ":asynchronous-mail:1.2"
      //  compile ":mail:1.0.7"

        // compile "org.grails.plugins:google-chart:0.5.2"
      //  compile "org.grails.plugins:jquery-validation-ui:1.4.9"
        compile ":action-logging:1.1.1"
       // compile "org.grails.plugins:glassfish:0.1.2"
    //    compile "org.grails.plugins:serializable-session:0.5"
       // compile ":nexmo:1.0"



    }
    inherits("global") {
        excludes 'log4j', 'jcl-over-slf4j', 'slf4j-api', 'slf4j-log4j12','SLF4J'
    }
}
