
function chatLoad(loaded) {
    $('#chat-container').load('chat.html', function() {
        chatLoaded(loaded);
    });
}

function chatLoaded(loaded) {
    $('.chatNew-clickable').click(chatClick);
    loaded('chat');
}

function chatClickable() {
    return state.chatContact !== null;
}

function chatClick() {
    if (!chatClickable()) {
        console.warn('chatClick not ready');
    }
}