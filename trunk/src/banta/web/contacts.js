
function contactsArray(o) {
    return [o.name];
};

function contactsLoad(loadedCallback) {
    $('#contacts-container').load('contacts.html', function() {
        contactEditLoaded(loadedCallback);
    });
}

function contactsLoaded(loadedCallback) {
    $('.contacts-clickable').click(contactsClick);
    loadedCallback('contacts');
}

function contactsClickable() {
    return !isEmpty(state.contacts);
}

function contactsClick() {
    if (assertTrue('contacts', contactsClickable())) {
        console.log("contactsClick");
        setPath('contacts');
        contactsBuild(state.contacts);
        $('#title').text('Contacts');
        $('.page-container').hide();
        $('#contacts-container').show();
    }
}

function contactsSort(array) {
    array.sort(compareName);
}

function contactsIndexOf(name) {
    return arrayIndexOf(state.contacts, name, matchName);
}

function contactsBuild(array) {
    contactsSort(array);
    buildTable($('#contacts-tbody'), contactsArray, array);
    $("#contacts-tbody > tr").click(function() {
        contactsListRowClick($(this).children('td').first().text());
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

