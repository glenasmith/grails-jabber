import org.codehaus.groovy.grails.jabber.ChatListener

import org.apache.log4j.Logger

/**
 * Created by IntelliJ IDEA.
 * User: glen
 * Date: Oct 2, 2008
 * Time: 8:28:57 PM
 * To change this template use File | Settings | File Templates.
 */
class ChatListenerTests extends GroovyTestCase {

    private static final Logger log = Logger.getLogger(ChatListenerTests.class)

    void NOtestSend() {

        /*
        ChatListener cl = new ChatListener(host: 'talk.google.com', serviceName: 'gmail.com',
                                userName: 'bytecode.com.au@gmail.com', password: 'tanstaafl')
        */

        ChatListener cl = new ChatListener(host: 'localhost', // serviceName: 'decaf.local',
                                userName: 'glen', password: 'password')

        cl.connect()

        cl.sendJabberMessage("peter@decaf.local", "Just a test case... at ${ new Date() }")

        cl.disconnect()

    }


    void NOtestListen() {

        ChatListener cl = new ChatListener(host: 'localhost', // serviceName: 'decaf.local',
                                userName: 'glen', password: 'password'
                           )

        cl.connect()
        cl.addListener()

        cl.addListener { msg ->
            log.error "From ${msg.from} with content ${msg.body}"
        }
        cl.sendJabberMessage("peter@decaf.local", "Starting a chat...")
        println "Waiting..."
        Thread.currentThread().sleep(30000)
        cl.disconnect()
        

    }



}