
b.contacts = {
    page: 'contacts',
    path: 'contacts',
    defaultTitle: 'Contacts',
    purposeTitle: {
        chat: 'Contact to chat',
        eventHost: 'Event host',
        eventInvitee: 'Invitee',
    },
    dom: {
        input: '#contacts-search-input',
        form: '#contacts-search-form',
        tbody: '#contacts-tbody',
    },
    loaded: function() {
        foreachKey(b.contacts.dom, function(key, value) {
            b.contacts['$' + key] = $(value);
            if (value.endsWith('-clickable')) {
                b.contacts['$' + key].click(b.contacts[key + 'Click']);
            }
        });
        $('.contacts-clickable').click(b.contacts.click);
        $('.contacts-new-clickable').click(b.contacts.newClick);
        $('.contacts-edit-clickable').click(b.contacts.editClick);
        $('.contacts-reduce-clickable').click(b.contacts.reduceClick);
        $('.contacts-expand-clickable').click(b.contacts.expandClick);
        $('.contacts-search-clickable').click(b.contacts.searchClick);
        $('.contacts-clear-clickable').click(b.contacts.clearClick);
        $('.contacts-ok-clickable').click(b.contacts.okClick);
        b.contacts.$form.submit(b.contacts.searchClick);
    },
    newClick: function() {
        contactNewClick();
    },
    editClick: function() {
        if (state.selectedContacts.length === 1) {
            contactEdit(state.selectedContacts[0]);
        }
    },
    click: function() {
        console.log("contacts.click", state.purpose);
        b.contacts.reset();
        b.contacts.showPage();
    },
    reset: function() {
        state.availableContacts = state.contacts.slice(0);
        state.selectedContacts = [];
        state.chosenContacts = null;
        state.chosenContact = null;
    },
    showPage: function() {
        console.log("contacts.click", state.purpose, state.availableContacts);
        state.contact = null;
        b.contacts.build(state.availableContacts, null);
        b.contacts.title = b.contacts.defaultTitle;
        if (!isEmpty(state.purpose)) {
            b.contacts.title = b.contacts.purposeTitle[state.purpose];
        }
        showPageObj(b.contacts, null);
        b.contacts.$input.focus();
    },
    build: function(contacts, search) {
        console.log('contacts.build', contacts.length, state.selectedContacts.length);
        contacts.sort(compareName);
        b.contacts.$tbody.empty();
        for (var i = 0; i < contacts.length; i++) {
            console.log('contacts.build for', i, contacts[i].name);
            if (u.array.contains(state.selectedContacts, contacts[i]) ||
                    isEmpty(search) || contacts[i].name.startsWithIgnoreCase(search)) {
                b.contacts.buildRow(contacts[i]);
            }
        }
    },
    buildRow: function(contact) {
        b.contacts.$tbody.append('<tr><td>' + contact.name + '</td></tr>');
        var tr = b.contacts.$tbody.children('tr:last');
        tr.click(contact, b.contacts.contactRowClick);
        if (u.array.contains(state.selectedContacts, contact)) {
            tr.children().addClass('selected');
        } else {
            tr.children().removeClass('selected');
        }
    },
    okClick: function(event) {
        console.log('okClick');
        event.preventDefault();
        if (state.chosenContacts) {
            state.chosenContacts(state.selectedContacts);
        }
    },
    reduceClick: function(event) {
        console.log('reduceClick');
        render(event, state.selectedContacts);
    },
    expandClick: function(event) {
        console.log('expandClick');
        render(event);
    },
    clearClick: function(event) {
        console.log('clearClick');
        state.selectedContacts = [];
        render(event);
    },
    searchClick: function(event) {
        console.log('searchClick', event);
        render(event);
    },
    render: function(event, contacts) {
        event.preventDefault();
        if (!contacts) {
            contacts = state.availableContacts;
        }
        b.contacts.build(contacts, b.contacts.$input.val());
        b.contacts.$input.val('');
        b.contacts.$input.focus();
    },
    choose: function(purpose, chosenCallback, availableContacts) {
        state.purpose = purpose;
        state.chosenContact = chosenCallback;
        state.chosenContacts = null;
        state.availableContacts = availableContacts;
        state.selectedContacts = [];
        if (!u.object.containsKey(b.contacts.purposeTitle, purpose)) {
            console.warn('choose', state.purpose);
        }
        b.contacts.showPage();
    },
    chooseMulti: function(purpose, chosenCallback, selectedContacts, availableContacts) {
        b.contacts.reset();
        state.purpose = purpose;
        state.chosenContacts = chosenCallback;
        state.selectedContacts = selectedContacts;
        if (availableContacts) {
            state.availableContacts = availableContacts;
        }
        if (!u.object.containsKey(b.contacts.purposeTitle, purpose)) {
            console.warn('chooseMulti', state.purpose);
        }
        b.contacts.showPage();
    },
    contactRowClick: function(event) {
        state.contact = event.data;
        if (isEmpty(state.purpose)) {
            contactEdit(state.contact);
        } else if (!isEmpty(state.chosenContact)) {
            state.chosenContact(state.contact);
        } else {
            var td = $(this).children('td').first();
            if (td.hasClass('selected')) {
                td.removeClass('selected');
                u.array.remove(state.selectedContacts, state.contact);
            } else {
                td.addClass('selected');
                state.selectedContacts.push(state.contact);
            }
        }
        console.log('contacts.contactRowClick', state.purpose, state.contact, state.selectedContacts);
    },
};

