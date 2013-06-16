
function chatLoad(loaded) {
    $('#chat-container').load('chat.html', function() {
        chatLoaded(loaded);
    });
}

function chatLoaded(loaded) {
    $('.chatNew-clickable').click(chatClick);
    loaded('chat');
    chatTest();
}

function chatClickable() {
    return state.chatContact !== null;
}

function chatClick() {
    if (!chatClickable()) {
        console.warn('chatClick not ready');
    }
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