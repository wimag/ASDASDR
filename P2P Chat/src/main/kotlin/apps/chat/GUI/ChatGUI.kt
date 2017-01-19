package apps.chat.GUI


import apps.chat.Chat
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

/**
 * Simple chat gui. Almost entirely found on internet
 */
class ChatGUI(internal var chat: Chat) {

    internal var appName: String = "P2P chat window(loading)"
    internal var chatFrame = JFrame(appName)
    internal var sendMessage = JButton("Send Message")

    internal var messageBox: JTextField = JTextField()
    internal var chatBox: JTextArea = JTextArea()
    var isClosed: Boolean = false



    fun display() {
        val mainPanel = JPanel()
        mainPanel.layout = BorderLayout()

        val southPanel = JPanel()
        southPanel.background = Color.BLUE
        southPanel.layout = GridBagLayout()

        val northPanel = JPanel()
        northPanel.background = Color.BLUE
        northPanel.layout = GridBagLayout()

        val listener = sendMessageButtonListener()

        messageBox = JTextField(30)
        messageBox.requestFocusInWindow()
        messageBox.addActionListener(listener)


        sendMessage.addActionListener(listener)


        chatBox = JTextArea()
        chatBox.isEditable = false
        chatBox.font = Font("Serif", Font.PLAIN, 15)
        chatBox.lineWrap = true

        mainPanel.add(JScrollPane(chatBox), BorderLayout.CENTER)

        val left = GridBagConstraints()
        left.anchor = GridBagConstraints.LINE_START
        left.fill = GridBagConstraints.HORIZONTAL
        left.weightx = 512.0
        left.weighty = 1.0

        val right = GridBagConstraints()
        right.insets = Insets(0, 10, 0, 0)
        right.anchor = GridBagConstraints.LINE_END
        right.fill = GridBagConstraints.NONE
        right.weightx = 1.0
        right.weighty = 1.0

        southPanel.add(messageBox, left)
        southPanel.add(sendMessage, right)


        mainPanel.add(BorderLayout.NORTH, northPanel)
        mainPanel.add(BorderLayout.SOUTH, southPanel)

        chatFrame.add(mainPanel)
        chatFrame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        chatFrame.setSize(600, 350)
        chatFrame.isVisible = true
        chatFrame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                isClosed = true
            }
        })
    }

    fun reopen() {
        isClosed = false
        chatFrame.isVisible = true
    }

    fun refreshTitle(title: String) {
        appName = title
        chatFrame.title = appName
    }

    fun displayMessage(user: String, msg: String) {
        chatBox.append("<$user>:  $msg\n")
    }


    internal inner class sendMessageButtonListener : ActionListener {
        override fun actionPerformed(event: ActionEvent) {
            if (messageBox.text.length < 1) {
                // do nothing
            } else if (messageBox.text.equals(".clear")) {
                chatBox.text = "Cleared all messages\n"
                messageBox.text = ""
            } else {
                chat.sendMessage(messageBox.text)
                messageBox.text = ""
            }
            messageBox.requestFocusInWindow()
        }
    }

    fun getUserInput(description: String): String {
        return JOptionPane.showInputDialog(chatFrame, description,
                "User input request", JOptionPane.QUESTION_MESSAGE)
    }


}