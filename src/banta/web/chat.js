
function chatLoad(loaded) {
    $('#chat-container').load('chat.html', function() {
        chatLoaded(loaded);
    });
}

function chatLoaded(loaded) {
    $('.chat-clickable').click(chatClick);
    loaded('chat');
    chatTest();
}

function chatClickable() {
    return state.chat !== null;
}

function chatClick() {
    if (!chatClickable()) {
        console.warn('chatClick not ready');
    } else {
        showPageId('chat', 'Chat', chat.name);
    }
}

function chat(chat) {
    console.log('chat', chat);
    state.chat = chat;
    showPage('Chat', 'chat', chat.name);
}

function chatRes(event) {
    console.log('chatRes', event.data.name, event.data.text);    
}

function chatTest() {
    var data = {
        name: 'Joe',
        text: 'Hello'
    };
    server.send('chat', data);
}
