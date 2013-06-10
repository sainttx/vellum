
function contactsArray(o) {
    return [o.name];
};

function contactsReady() {
    console.log("contactsReady");
    $('.contacts-clickable').click(contactsClick);
}

function contactsClick() {
    console.log("contactsClick", window.location);
    window.history.pushState(null, null, "contacts");
    contactsBuild(state.contacts);
    $('#title').text('Contacts');
    $('.page-container').hide();
    $('#contacts-container').show();
}

function contactsSort(array) {
    array.sort(function(a, b) {
        if (a.name === b.name) {
            return 0;
        } else if (a.name.toLowerCase() > b.name.toLowerCase()) {
            return 1;
        }
        return -1;
    });    
}

function contactsBuild(array) {
    contactsSort(array);
    buildTable($('#contacts-tbody'), contactsArray, array);
    $("#contacts-tbody > tr").click(function() {
        contactsListRowClick($(this).children('td').first().text());
    });    
}

function contactsIndexOf(id) {
    return arrayIndexOf(state.contacts, id, function(object, id) {
        return object.name === id;
    });
    
}
function contactsPut(contact) {
    if (state.contact) {
        var index = contactsIndexOf(state.contact.name);
        if (index >= 0) {
            state.contacts[index] = contact;
        }
    } else {
        var index = contactsIndexOf(contact.name);
        if (index !== null && index >= 0) {
            console.log('contactsPut', contact.name, index);
            state.contacts[index] = contact;
        } else {
            state.contacts.push(contact);
        }
    }
}

function contactsListRowClick(id) {
    var index = contactsIndexOf(id);
    if (index >= 0) {
        contactEdit(state.contacts[index]);
    }
}

