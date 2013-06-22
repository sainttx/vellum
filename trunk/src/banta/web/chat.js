
function chatClick() {
    if (state.chat) {
        chat(state.chat);
    } else if (state.contact) {
        console.warn('chatClick', state.contact);
    } else {
        state.action = 'chat';
        contactsClick();
    }
}

function chatLoaded() {
    dom.chat = {};
    dom.chat.tbody = $('#chat-tbody');
    dom.chat.trHtml = dom.chat.tbody.html();   
    $('#chat-draft-input').keypress(function(event) {
       console.log('keypress', event);
    });
}

function chat(chat) {
    console.log('chat', chat);
    state.chat = chat;
    chatBuild();
    showPage(chat.name, 'chat', 'chat', chat.name);
}

function chatBuild() {
    dom.chat.tbody.empty();
    for (var i = 0; i < state.chat.messages.length; i++) {
        dom.chat.tbody.append(dom.chat.trHtml);
        var tr = dom.chat.tbody.children('tr:last-child');
        console.log('chatBuild', state.chat.messages[i]);
        if (state.chat.messages[i].contact) {
            tr.find('span.chat-contact').text(state.chat.messages[i].contact.name);
        } else {
            tr.find('span.chat-contact').text('');
        }
        tr.find('span.chat-time').text(formatDate(state.chat.messages[i].time));
        tr.find('span.chat-message').text(state.chat.messages[i].textMessage);
        console.log('chatBuild', state.chat.messages[i].textMessage);
        tr.click(state.chat.messages[i], chatsRowClick);
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
