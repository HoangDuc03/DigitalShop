'use strict';

var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    event.preventDefault();
}

function onConnected() {

    stompClient.subscribe('/chatbox', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.register",
        {},
        JSON.stringify({username: username , type: 'JOIN'}
        )
    )
}


function onError(error) {
    connectingElement.textContent = 'Error';
    connectingElement.style.color = 'red';
}


function send(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            username: username,
            message: messageInput.value,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement;
    if(message.type === 'CHAT') {
        messageElement = document.createElement('li');
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.username[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = 'rgb(255, 86, 82)';

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.username);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.message);
    if(message.message != null)
        {
            textElement.appendChild(messageText);
            messageElement.appendChild(textElement);

            messageArea.appendChild(messageElement);
        }


    messageArea.scrollTop = messageArea.scrollHeight;
}

messageForm.addEventListener('submit', send)
document.addEventListener('DOMContentLoaded',connect);
