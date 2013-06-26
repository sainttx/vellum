
b.contacts = {
    page: 'contacts',
    path: 'contacts',
    defaultTitle: 'Contacts',
    purposeTitle: {
        chat: 'Contact to chat',
        eventHost: 'Event host',
        eventInvite: 'Invitee',
    },
    dom: {
        input: '#contacts-search-input',
        form: '#contacts-search-form',
        search: '.contacts-search-clickable',
        clear: '.contacts-search-clear-clickable',
        tbody: '#contacts-tbody',
    },
    clearClick: function() {
        console.log('clearClick');
        event.preventDefault();
        b.contacts.$input.val('');
        b.contacts.build(state.contacts, b.contacts.$input.val());
        b.contacts.$input.focus();
    },
    searchClick: function(event) {
        console.log('searchClick');
        event.preventDefault();
        b.contacts.build(state.contacts, b.contacts.$input.val());
        b.contacts.$input.val('');
        b.contacts.$input.focus();
    },
    loaded: function() {
        foreachKey(b.contacts.dom, function(key, value) {
            b.contacts['$' + key] = $(value);
            if (value.endsWith('-clickable')) {
                b.contacts['$' + key].click(b.contacts[key + 'Click']);
            }
        });
        $('.contacts-clickable').click(b.contacts.click);
        b.contacts.$form.submit(b.contacts.searchClick);
    },
    click: function() {
        console.log("contacts.click", state.purpose);
        state.contact = null;
        b.contacts.build(state.contacts, null);
        b.contacts.title = b.contacts.defaultTitle;
        if (!isEmpty(state.purpose)) {
            b.contacts.title = b.contacts.purposeTitle[state.purpose];
        }
        showPageObj(b.contacts, null);
        b.contacts.$input.focus();
    },
    build: function(contacts, filter) {
        contacts.sort(compareName);
        b.contacts.$tbody.empty();
        for (var i = 0; i < contacts.length; i++) {
            if (isEmpty(filter) || contacts[i].name.startsWith(filter)) {
                b.contacts.$tbody.append('<tr><td>' + contacts[i].name + '</td></tr>');
                b.contacts.$tbody.children('tr:last').click(contacts[i], b.contacts.contactRowClick);
            }
        }
    },
    choose: function(purpose, chosen) {
        state.purpose = purpose;
        state.chosen = chosen;
        if (!u.object.containsKey(b.contacts.purposeTitle, purpose)) {
            console.warn('choose', state.purpose);
        }
        b.contacts.click();
    },
    contactRowClick: function(event) {
        console.log('contacts.contactRowClick', state.purpose);
        if (!isEmpty(state.purpose) && !isEmpty(state.chosen)) {
            state.chosen(event.data);
        } else {
            contactEdit(event.data);
        }
    },
    put: function(contact) {
        if (state.contact) {
            var index = u.array.indexOf(state.contacts, state.contact.name, matchName);
            console.log('put', state.contact.name, index);
            if (index >= 0) {
                state.contacts[index] = contact;
            }
        } else {
            var index = u.array.indexOf(state.contacts, contact.name, matchName);
            if (index && index >= 0) {
                console.log('put', contact.name, index);
                state.contacts[index] = contact;
            } else {
                state.contacts.push(contact);
            }
        }
    },
};

