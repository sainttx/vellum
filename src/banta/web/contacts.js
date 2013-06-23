

function contactsLoaded() {
    dom.contacts = {};
}

function contactsClick() {
    console.log("contactsClick", state.action);
    state.contact = null;
    contactsBuild($('#contacts-tbody'), state.contacts);
    var title = 'Contacts';
    if (state.action && state.action === 'chat') {
        title = 'Contact to chat';
    }
    showPage(title, 'contacts', 'contacts', null);
}

function contactsIndexOf(name) {
    return arrayIndexOf(state.contacts, name, matchName);
}

function contactsBuild(tbody, contacts) {
    Array.sort(contacts, compareName);
    tbody.empty();
    for (var i = 0; i < contacts.length; i++) {
        $('#contacts-tbody').append('<tr><td>' + contacts[i].name + '</td></tr>');
        $("#contacts-tbody > tr:last-child").click(contacts[i], contactsRowClick);
    }
}

function contactsPut(contact) {
    if (state.contact) {
        var index = arrayIndexOf(state.contacts, state.contact.name, matchName);
        console.log('contactsPut', state.contact.name, index);
        if (index >= 0) {
            state.contacts[index] = contact;
        }
    } else {
        var index = arrayIndexOf(state.contacts, contact.name, matchName);
        if (index && index >= 0) {
            console.log('contactsPut', contact.name, index);
            state.contacts[index] = contact;
        } else {
            state.contacts.push(contact);
        }
    }
}

function contactsRowClick(event) {
    contactEdit(event.data);
}
