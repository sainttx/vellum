
function chatsArray(o) {
    return [o.name];
};

function chatsReady() {
    console.log("chatsReady");
    $('.chats-clickable').click(chatsClick);
}

function chatsClickable() {
    return state.chats !== null;
}

function chatsClick() {
    console.log("chatsClick");
    window.history.pushState(null, null, "/#chats");
    chatsBuild(state.chats);
    $('#title').text('Contacts');
    $('.page-container').hide();
    $('#chats-container').show();
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

function chatsBuild(array) {
    chatsSort(array);
    buildTable($('#chats-tbody'), chatsArray, array);
    $("#chats-tbody > tr").click(function() {
        chatsListRowClick($(this).children('td').first().text());
    });    
}

function chatsIndexOf(id) {
    return arrayIndexOf(state.chats, id, function(object, id) {
        return object.name === id;
    });
    
}
function chatsPut(contact) {
    if (state.contact) {
        var index = chatsIndexOf(state.contact.name);
        if (index >= 0) {
            state.chats[index] = contact;
        }
    } else {
        var index = chatsIndexOf(contact.name);
        if (index !== null && index >= 0) {
            console.log('chatsPut', contact.name, index);
            state.chats[index] = contact;
        } else {
            state.chats.push(contact);
        }
    }
}

function chatsListRowClick(id) {
    var index = chatsIndexOf(id);
    if (index >= 0) {
        contactEdit(state.chats[index]);
    }
}

