
b.contacts = {
    page: 'contacts',
    path: 'contacts',
    defaultTitle: 'Contacts',
    purposeTitle: {
        chat: 'Contact to chat',
        eventHost: 'Contact to host',
        eventInvite: 'Contact to invite',
    },
    dom: {
        input: '#contacts-search-input',
        tbody: '#contacts-tbody',
    },
};

function contactsLoaded() {
    foreachKey(b.contacts.dom, function(key, value) {
        b.contacts['$' + key] = $(value);
    });
}

function contactsClick() {
    console.log("contactsClick", state.purpose);
    state.contact = null;
    contactsBuild(state.contacts);
    b.contacts.title = b.contacts.defaultTitle;
    if (!isEmpty(state.purpose)) {
        b.contacts.title = b.contacts.purposeTitle[state.purpose];
    }
    showPageObj(b.contacts, null);
    b.contacts.$input.focus();
}

function contactsIndexOf(name) {
    return arrayIndexOf(state.contacts, name, matchName);
}

function contactsBuild(contacts) {
    contacts.sort(compareName);
    b.contacts.$tbody.empty();
    for (var i = 0; i < contacts.length; i++) {
        b.contacts.$tbody.append('<tr><td>' + contacts[i].name + '</td></tr>');
        b.contacts.$tbody.children('tr:last').click(contacts[i], contactsRowClick);
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
    if (state.purpose === 'chat') {
        chatNew(event.data);
    } else {
        contactEdit(event.data);
    }
}
