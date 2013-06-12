
function chatsArray(o) {
    return [o.name];
};

function chatsLoad(loaded) {
    $('#chats-container').load('chats.html', function() {
        chatsLoaded(loaded);
    });
}

function chatsLoaded(loaded) {
    $('.chats-clickable').click(chatsClick);
    loaded('chats');
}

function chatsClickable() {
    return !isEmpty(state.chats);
}

function chatsClick() {
    if (assertTrue('chatsClick', chatsClickable())) {
        window.history.pushState(null, null, "/#chats");
        chatsBuild(state.chats);
        $('#title').text('Chats');
        $('.page-container').hide();
        $('#chats-container').show();
    }
}

function chatsBuild(array) {
    chatsSort(array);
    buildTable($('#chats-tbody'), chatsArray, array);
    $("#chats-tbody > tr").click(function() {
        chatsListRowClick($(this).children('td').first().text());
    });    
}

function chatsSort(array) {
    array.sort(function(a, b) {
        if (a.name === b.name) {
            return 0;
        } else if (a.name.toLowerCase() > b.name.toLowerCase()) {
            return 1;
        }
        return -1;
    });    
}

function chatsIndexOf(id) {
    return arrayIndexOf(state.chats, id, function(object, id) {
        return object.name === id;
    });
    
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

function chatsListRowClick(id) {
    var index = chatsIndexOf(id);
    if (index >= 0) {
        chatEdit(state.chats[index]);
    }
}

