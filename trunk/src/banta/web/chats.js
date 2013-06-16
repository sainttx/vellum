
function chatsArray(o) {
    return [o.contacts[0].name, o.messages.last().textMessage, formatDate(o.messages.last().time)];
};

function chatsLoad(loaded) {
    $('#chats-container').load('chats.html', function() {
        chatsLoaded(loaded);
    });
}

function chatsLoaded(loaded) {
    $('.chats-clickable').click(chatsClick);
    $('#chats-tbody span').text('');
    dom.chats = {};
    dom.chats.tbody = $('#chats-tbody');
    dom.chats.trHtml = dom.chats.tbody.html();
    loaded('chats');
}

function chatsClickable() {
    return !isEmpty(state.chats);
}

function chatsClick() {
    console.log('chatsClick');
    if (!state.chats) {
        console.warn('chatsClick');
    } else {
        chatsBuild();
        showPage('Chats', 'chats');
    }
}

function chatsBuild() {
    state.chats.sort(compareName);
    dom.chats.tbody.empty();
    for (var i = 0; i < state.chats.length; i++) {
        dom.chats.tbody.append(dom.chats.trHtml);
        var tr = $("#chats-tbody > tr:last-child");
        console.log('chatsBuild', tr.find('span.chat-contact'));
        tr.find('span.chat-contact').text(state.chats[i].name);
        tr.find('span.chat-time').text(formatDate(arrayLast(state.chats[i].messages).time));
        tr.find('span.chat-message').text(arrayLast(state.chats[i].messages).textMessage);
        tr.click(state.chats[i], function(event) {
            chatsRowClick(event);
        });
    }
}

function chatsPut(chat) {
    if (state.chat) {
        var index = chatsIndexOf(state.chat.name);
        if (index >= 0) {
            state.chats[index] = chat;
        }
    } else {
        var index = chatsIndexOf(chat.name);
        if (index !== null && index >= 0) {
            console.log('chatsPut', chat.name, index);
            state.chats[index] = chat;
        } else {
            state.chats.push(chat);
        }
    }
}

function chatsRowClick(event) {
    console.log('chatsRowClick', event.data);
    chat(event.data);
}

