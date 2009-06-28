import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CFG

import org.codehaus.groovy.grails.jabber.ChatListener


class JabberGrailsPlugin {
    def version = 0.1
    def dependsOn = [:]

    def author = "Glen Smith"
    def authorEmail = "glen@bytecode.com.au"
    def title = "Jabber Plugin"
    def description = '''\
This plugin provides the opportunity to send and receive Chat messages via the Jabber API.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/jabber"

    def doWithSpring = {

        boolean listenerDefined = false

        application.serviceClasses?.each { service ->
            def serviceClass = service.getClazz()
            def exposeList = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'expose')
            if (exposeList != null && exposeList.contains('jabber') && !listenerDefined) {
                println "adding Jabber listener for ${service.shortName} to Spring"

                def method = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'jabberListenerMethod')
                if (!method)
                    method = "onJabberMessage"

		"GrailsPluginJabberListener"(org.codehaus.groovy.grails.jabber.ChatListener) {
                    host = CFG.config.chat.host
                    port = CFG.config.chat.port
                    serviceName = CFG.config.chat.serviceName
                    userName = CFG.config.chat.username
                    password = CFG.config.chat.password
                    listenerMethod = method
                    targetService = ref("${service.propertyName}")

                }
                listenerDefined = true

				
            }
        }
        if (!listenerDefined) {

            "GrailsPluginJabberListener"(org.codehaus.groovy.grails.jabber.ChatListener) {
                    host = CFG.config.chat.host
                    port = CFG.config.chat.port
                    serviceName = CFG.config.chat.serviceName
                    userName = CFG.config.chat.username
                    password = CFG.config.chat.password
             }

        }

    }
   
    def doWithApplicationContext = { applicationContext ->

        boolean listenerStarted = false
        application.serviceClasses?.each { service ->
            def serviceClass = service.getClazz()
            def exposeList = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'expose')
            if (exposeList!=null && exposeList.contains('jabber') && !listenerStarted) {
                println "Starting Jabber listener for ${service.shortName}"
                def listener = applicationContext.getBean("GrailsPluginJabberListener")
                listener.listen()
                println "Added listener ok ${listener.dump()}"
                listenerStarted = true
            }
        }


    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }

    def sendJabberMessage = {cl, to, message ->
    	
    	if (to instanceof List) {
    	    to.each { addr ->
                cl.sendJabberMessage(addr, message)    
            }
        } else {
            cl.sendJabberMessage(to, message)
        }

    }
	                                      
    def doWithDynamicMethods = { ctx ->

        ChatListener cl = new ChatListener(host: CFG.config.chat.host,
            serviceName: CFG.config.chat.serviceName,
            userName: CFG.config.chat.username,
            password: CFG.config.chat.password
        )

        def listener = applicationContext.getBean("GrailsPluginJabberListener")

        application.serviceClasses?.each { service ->
            def mc = service.metaClass          
            mc.sendJabberMessage = sendJabberMessage.curry(listener)
        }
        application.controllerClasses?.each { controller ->
            def mc = controller.metaClass
            mc.sendJabberMessage = sendJabberMessage.curry(listener)
    	}

    }
	
    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
