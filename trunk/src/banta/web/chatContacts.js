
function chatContactsLoad(loadedCallback) {
    $('#chatContacts-container').load('chatContacts.html', function() {
        contactEditLoaded(loadedCallback);
    });
}

function chatContactsLoaded(loadedCallback) {
    dom.chatContacts = {};
    loadedCallback('chatContacts');
}

function chatContactsClick() {
    chatContactsBuild($('#chatContacts-tbody'), state.contacts);
    showPage('Chat contacts', 'chatContacts', 'chatContacts', null);
}

function chatContactsBuild(tbody, contacts) {
    contacts.sort(compareName);
    tbody.empty();
    for (var i = 0; i < contacts.length; i++) {
        tbody.append('<tr><td>' + contacts[i].name + '</td></tr>');
        tbody.children('tr:last-child').click(contacts[i], chatContactsRowClick);
    }
}

function chatContactsRowClick(event) {
    chat(event.data);
}
