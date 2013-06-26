
b.chats = {
    matcher: function(object, data) {
        return object.name === data;
    },
    loaded: function() {
        $('#chats-tbody span').text('');
        $('.chats-clickable').click(chatsClick);
        $('.chat-new-clickable').click(b.chat.clickNew);
        dom.chats = {};
        dom.chats.tbody = $('#chats-tbody');
        dom.chats.trHtml = dom.chats.tbody.html();
    },
};


function chatsClick() {
    console.log('chatsClick');
    if (!state.chats) {
        console.warn('chatsClick');
    } else {
        chatsBuild();
        showPage('Chats', 'chats', 'chats', null);
    }
}

function chatsBuild() {
    state.chats.sort(compareName);
    dom.chats.tbody.empty();
    for (var i = 0; i < state.chats.length; i++) {
        dom.chats.tbody.append(dom.chats.trHtml);
        var tr = $("#chats-tbody > tr:last-child");
        tr.find('span.chats-contact').text(state.chats[i].name);
        tr.find('span.chats-time').text(u.date.format(arrayLast(state.chats[i].messages).time));
        tr.find('span.chats-message').text(arrayLast(state.chats[i].messages).textMessage);
        tr.click(state.chats[i], chatsRowClick);
    }
}

function chatsPut(chat) {
    if (state.chat) {
        var index = u.array.indexOf(state.chats, state.chat.name, b.chats.matcher);
        if (index >= 0) {
            state.chats[index] = chat;
        }
    } else {
        var index = u.array.indexOf(state.chats, chat.name, b.chats.matcher);
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
    b.chat.chat(event.data);
}

